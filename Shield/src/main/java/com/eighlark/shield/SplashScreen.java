package com.eighlark.shield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SplashScreen extends Activity {

    private static final String TAG = "SplashScreen";

    private static final long SPLASH_SCREEN_DELAY = 2000;

    // Parse Object to store loggedIn User.
    ParseUser parseUser;

    // UI Handles
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force SplashScreen to start with No Title and Fullscreen mode
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        ParseAnalytics.trackAppOpened(getIntent());

        loadingText = (TextView) findViewById(R.id.loadingText);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handleLogin();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        } catch (NullPointerException ex) {
            handleLogin();
        }
    }

    private void handleLogin() {

        parseUser = ParseUser.getCurrentUser();

        if (parseUser == null) {
            updateUI(false /* isSignedIn */, false /* isFirstLaunch */);
            ParseFacebookUtils.logIn(this, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Log.i(TAG, "Uh oh. The user cancelled the Facebook login.");
                        updateUI(false /* isSignedIn */, false /* isFirstLaunch */);
                    } else if (user.isNew()) {
                        Log.i(TAG, "User signed up and logged in through Facebook!");
                        parseUser = user;
                        updateUI(true /* isSignedIn */, true /* isFirstLaunch */);
                    } else {
                        Log.i(TAG, "User logged in through Facebook!");
                        parseUser = user;
                        updateUI(true /* isSignedIn */, false /* isFirstLaunch */);
                    }
                }
            });

        } else {
            Log.i(TAG, "Username: " + parseUser.get("firstname"));
            updateUI(true /* isSignedIn */, false /* isFirstLaunch */);
        }

    }

    private void updateUI(boolean isSignIn, boolean isFirstLaunch) {
        if (isFirstLaunch) {
            Session session = ParseFacebookUtils.getSession();
            if (session.isOpened()) {
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        parseUser.put("firstname", user.getFirstName());
                        parseUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                loadingText.setText(
                                        getString(R.string.status_welcome) +
                                                " " + parseUser.get("firstname"));
                                launchApplication();
                            }
                        });
                    }
                });
            }
        } else if (isSignIn) {
            loadingText.setText(
                    getString(R.string.status_welcome) + " " + parseUser.get("firstname"));
            launchApplication();
        } else {
            loadingText.setText(getString(R.string.status_loading));
        }
    }

    private void launchApplication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                                        /* Create an Intent that will start the Main-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }
}