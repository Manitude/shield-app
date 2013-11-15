package com.eighlark.shield;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;

import com.eighlark.shield.auth.AuthUtil;
import com.eighlark.shield.fragments.PlusClientFragment;
import com.eighlark.shield.model.User;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.parse.Parse;
import com.testflightapp.lib.TestFlight;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   13/11/13
 */
public class BaseActivity extends ActionBarActivity
        implements
        PlusClientFragment.OnSignInListener,
        View.OnClickListener {

    /**
     * Code used to identify the login request to the {@link PlusClientFragment}
     * .
     */
    public static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;

    /** Delegate responsible for handling Google sign-in. */
    protected PlusClientFragment sPlusClientFragment;

    /** Used to retrieve the PhotoHunt back end session id. */
    private AsyncTask<Object, Void, User> sUserAsyncAuthTask;

    /** Person as returned by Google Play Services. */
    protected Person sPlusPerson;

    /** Profile as returned by the Shield service. */
    protected User sShieldUser;

    /**
     * Stores the pending click listener which should be executed if the user
     * successfully authenticates. {@link #sPendingClick} is populated if a user
     * performs an action which requires authentication but has not yet
     * successfully authenticated.
     */
    protected View.OnClickListener sPendingClick;

    /**
     * Stores the {@link View} which corresponds to the pending click listener
     * and is supplied as an argument when the action is eventually resolved.
     */
    protected View sPendingView;

    /**
     * Stores the @link com.google.android.gms.common.SignInButton} for use in
     * the action bar.
     */
    protected SignInButton sSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestFlight.takeOff(getApplication(), "9dcc57b4-6e07-4c7e-88bb-a2a8782db7fd");

        // Initialize Parse Library
        Parse.initialize(this,
                "KQwLyizN30RXqIGE7jXqbgdeRf8WnpqPUd84ua9E" /* APP ID */,
                "WfLNlgXJC3OfZ45FenhoGbHtrboLmrIhsrTfDpXv" /* Client Key */);

        // Create the PlusClientFragment which will initiate authentication if
        // required.
        // AuthUtil.SCOPES describe the permissions that we are requesting of
        // the user to access
        // their information and write to their moments vault.
        // AuthUtil.VISIBLE_ACTIVITIES describe the types of moment which we can
        // read from or write
        // to the user's vault.
        sPlusClientFragment = PlusClientFragment.getPlusClientFragment(this, AuthUtil.SCOPES,
                AuthUtil.VISIBLE_ACTIVITIES);

        sSignInButton = (SignInButton) getLayoutInflater().inflate(
                R.layout.sign_in_button, null);
        sSignInButton.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        // Delegate onActivityResult handling to PlusClientFragment to resolve
        // authentication
        // failures, eg. if the user must select an account or grant our
        // application permission to
        // access the information we have requested in AuthUtil.SCOPES and
        // AuthUtil.VISIBLE_ACTIVITIES
        sPlusClientFragment.handleOnActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Reset any asynchronous tasks we have running.
        resetTaskState();
    }

    /**
     * Invoked when the {@link PlusClientFragment} delegate has successfully
     * authenticated the user.
     *
     * @param plusClient
     *            The connected PlusClient which gives us access to the Google+
     *            APIs.
     */
    @Override
    public void onSignedIn(PlusClient plusClient) {
        if (plusClient.isConnected()) {
            sPlusPerson = plusClient.getCurrentPerson();

            // Retrieve the account name of the user which allows us to retrieve
            // the OAuth access
            // token that we securely pass over to the PhotoHunt service to
            // identify and
            // authenticate our user there.
            final String name = plusClient.getAccountName();

            // Asynchronously authenticate with the PhotoHunt service and
            // retrieve the associated
            // PhotoHunt profile for the user.
            sUserAsyncAuthTask = new AsyncTask<Object, Void, User>() {
                @Override
                protected User doInBackground(Object... o) {
                    return AuthUtil.authenticate(BaseActivity.this, name);
                }

                @Override
                protected void onPostExecute(User result) {
                    if (result != null) {
                        setAuthenticatedProfile(result);
                        executePendingActions();
                        update();
                    } else {
                        setAuthenticatedProfile(null);
                        sPlusClientFragment.signOut();
                    }
                }
            };

            sUserAsyncAuthTask.execute();
        }
    }

    /**
     * Invoked when the {@link PlusClientFragment} delegate has failed to
     * authenticate the user. Failure to authenticate will often mean that the
     * user has not yet chosen to sign in.
     *
     * The default implementation resets the PhotoHunt profile to null.
     */
    @Override
    public void onSignInFailed() {
        setAuthenticatedProfile(null);
        update();
    }

    /**
     * Invoked when the PhotoHunt profile has been successfully retrieved for an
     * authenticated user.
     *
     * @param profile
     */
    public void setAuthenticatedProfile(User profile) {
        sShieldUser = profile;
    }

    /**
     * Update the user interface to reflect the current application state. This
     * function is called whenever this Activity's state has been modified.
     *
     * {@link BaseActivity} calls this method when user authentication succeeds
     * or fails.
     */
    public void update() {
        supportInvalidateOptionsMenu();
    }

    /**
     * Execute actions which are pending; eg. because they were waiting for the
     * user to authenticate.
     */
    protected void executePendingActions() {
        // On successful authentication we resolve any pending actions
        if (sPendingClick != null) {
            sPendingClick.onClick(sPendingView);
            sPendingClick = null;
            sPendingView = null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            sPlusClientFragment.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
        }
    }

    /**
     * Provides a guard to ensure that a user is authenticated.
     */
    protected boolean requireSignIn() {
        if (!sPlusClientFragment.isAuthenticated()) {
            sPlusClientFragment.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return true if the user is currently authenticated through Google
     *         sign-in and the the user's PhotoHunt profile has being fetched.
     */
    public boolean isAuthenticated() {
        return sPlusClientFragment.isAuthenticated() && sShieldUser != null;
    }

    /**
     * @return true if the user is currently being authenticated through Google
     *         sign-in or if the the user's PhotoHunt profile is being fetched.
     */
    public boolean isAuthenticating() {
        return sPlusClientFragment.isConnecting() || sPlusClientFragment.isAuthenticated()
                && sShieldUser == null;
    }

    /**
     * Resets the state of asynchronous tasks used by this activity.
     */
    protected void resetTaskState() {
        if (sUserAsyncAuthTask != null) {
            sUserAsyncAuthTask.cancel(true);
            sUserAsyncAuthTask = null;
        }
    }
}
