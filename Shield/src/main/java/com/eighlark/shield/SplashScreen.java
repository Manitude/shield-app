package com.eighlark.shield;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eighlark.shield.domain.UserInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusClient;
import com.parse.ParseAnalytics;
import com.testflightapp.lib.TestFlight;

public class SplashScreen extends Activity
        implements
        View.OnClickListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = "SplashScreen";

    // Shared Preferences
    SharedPreferences sharedPreferences;

    // UserInfo Persistence Library
    private UserInfo userInfo;

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;
    private static final long SPLASH_SCREEN_DELAY = 2000;

    private PlusClient sPlusClient;
    private SignInButton sSignInButton;
    private LinearLayout splashScreenLoader;
    private TextView sLoadingText;
    private ConnectionResult sConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force SplashScreen to start with No Title and Fullscreen mode
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        sPlusClient = new PlusClient.Builder(this, this, this)
                .setVisibleActivities(
                        "http://schemas.google.com/AddActivity",
                        "http://schemas.google.com/BuyActivity")
                .build();

        sSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        splashScreenLoader = (LinearLayout) findViewById(R.id.splashscreen_loader);
        sLoadingText = (TextView) findViewById(R.id.loadingText);
        sSignInButton.setOnClickListener(this);

        ParseAnalytics.trackAppOpened(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        userInfo = new UserInfo(this);
        updateUI(View.INVISIBLE, View.VISIBLE, getString(R.string.status_loading));

        // Check if the application is running for the first time
        if (!userInfo.exists()) {
            Log.i(TAG, "First Run of Application");
            updateUI(View.VISIBLE, View.INVISIBLE, getString(R.string.status_loading));

        } else {
            Log.i(TAG, "Not the First Run of Application");

            // Start Application
            startApplication();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        updateUI(View.INVISIBLE, View.VISIBLE, getString(R.string.status_sign_in));
        if (!userInfo.exists()) {
            Log.i(TAG, "Saving user to shared preference");

            // Retrieve User Profile from PlusClient and save to shared Preference
            userInfo.setUSER_NAME(sPlusClient.getCurrentPerson().getDisplayName());
            userInfo.setEMAIL_ID(sPlusClient.getAccountName());
            userInfo.save();
        }
        startApplication();
    }

    @Override
    public void onDisconnected() {
        sPlusClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        sConnectionResult = connectionResult;
        this.onClick(sSignInButton);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                updateUI(View.INVISIBLE, View.VISIBLE, getString(R.string.status_sign_in));
                int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (available != ConnectionResult.SUCCESS) {
                    showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
                    return;
                }

                try {
                    sConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    // Fetch a new result to start.
                    sPlusClient.connect();
                } catch (NullPointerException e) {
                    sPlusClient.connect();
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
            return super.onCreateDialog(id);
        }

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return null;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
        }
        return new AlertDialog.Builder(this)
                .setMessage("Generic Error")
                .setCancelable(true)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN
                || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            if (resultCode == RESULT_OK && !sPlusClient.isConnected()
                    && !sPlusClient.isConnecting()) {
                // This time, connect should succeed.
                sPlusClient.connect();
            }
        }
    }

    private void startApplication() {
        if (userInfo.exists()) {
            updateUI(View.INVISIBLE, View.VISIBLE, getString(R.string.status_loading));
            Log.i(TAG, "User already created, Starting Activity");
            TestFlight.log(
                    this.getLocalClassName() +
                            ": " +
                            "Launching Main Activity");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
					/* Create an Intent that will start the Main-Activity. */
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }, SPLASH_SCREEN_DELAY);
        } else {
            updateUI(View.VISIBLE, View.INVISIBLE, getString(R.string.status_loading));
        }
    }

    private void updateUI(int SIGN_IN_BUTTON, int SPLASH_LOADER, String status_text) {
        sSignInButton.setVisibility(SIGN_IN_BUTTON);
        splashScreenLoader.setVisibility(SPLASH_LOADER);
        sLoadingText.setText(status_text);
    }
}