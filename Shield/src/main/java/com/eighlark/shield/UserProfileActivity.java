package com.eighlark.shield;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.eighlark.shield.fragments.UserProfileFragment;
import com.eighlark.shield.model.User;
import com.google.android.gms.common.SignInButton;

public class UserProfileActivity extends BaseActivity
        implements
        UserProfileFragment.UserProfileFragmentCallbacks {

    private static final String TAG = "UserProfileActivity";

    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfileFragment = UserProfileFragment.getUserProfileFragment(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, userProfileFragment).commit();
        }
    }

    /**
     * Callback to SignIn Button click event
     * {@link com.eighlark.shield.fragments.UserProfileFragment}
     */
    @Override
    public void onSignInClicked() {
        Log.i(TAG, "sign in clicked");
        /* Calls {@link BaseActivity.signIn} */
        signIn();
    }

    @Override
    public void onSetAuthenticatedProfile(User profile) {
        super.onSetAuthenticatedProfile(profile);
    }

    @Override
    public void update() {
        if (isAuthenticated()) {
            userProfileFragment.updateUI(true /* isSignedIn */);
        } else {
            userProfileFragment.updateUI(false /* isSignedIn */);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
