package com.eighlark.shield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.Arrays;
import java.util.List;

public class SplashScreen extends Activity {

    private static final String TAG = "SplashScreen";

    private static final long SPLASH_SCREEN_DELAY = 2000;

    // Parse Object to store loggedIn User.
    ParseUser currentUser;

    // UI Handles
    TextView loadingText;
    Button signInButton;
    LinearLayout splashScreenLoader;

    // Boolean


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

        loadingText = (TextView) findViewById(R.id.loading_text);
        splashScreenLoader = (LinearLayout) findViewById(R.id.splash_screen_loader);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Update UI */
                splashScreenLoader.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
                loadingText.setText(getString(R.string.status_logging_in));
                onLoginButtonClicked();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if there is a currently logged in user
        // and they are linked to a Facebook account.
        currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            /* Update UI */
            splashScreenLoader.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
            loadingText.setText(
                    String.format("%s %s",
                            getString(R.string.status_welcome),
                            currentUser.get("firstname")));

            // Go to the user info activity
            launchApplication();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void onLoginButtonClicked() {
        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                    /* Update UI */
                    splashScreenLoader.setVisibility(View.INVISIBLE);
                    signInButton.setVisibility(View.VISIBLE);
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");
                    currentUser = user;
                    createUser();
                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                    currentUser = user;
                    /* Update UI */
                    splashScreenLoader.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.INVISIBLE);
                    loadingText.setText(
                            String.format("%s %s",
                                    getString(R.string.status_welcome),
                                    currentUser.get("firstname")));
                    launchApplication();
                }
            }
        });
    }

    private void createUser() {
        if (currentUser != null) {
            Session session = ParseFacebookUtils.getSession();
            if (session != null) {
                Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        currentUser.put("firstname", user.getFirstName());
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                /* Update UI */
                                splashScreenLoader.setVisibility(View.VISIBLE);
                                signInButton.setVisibility(View.INVISIBLE);
                                loadingText.setText(
                                        String.format("%s %s",
                                                getString(R.string.status_welcome),
                                                currentUser.get("firstname")));
                                launchApplication();
                            }
                        });
                    }
                });
            }
        }
    }

    private void launchApplication() {
        Log.i(TAG, "Launching activity");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent that will start the Main-Activity.
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }
}