package com.eighlark.shield.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eighlark.shield.R;
import com.google.android.gms.common.SignInButton;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   16/11/13
 */
public class UserProfileFragment extends Fragment
        implements
        View.OnClickListener {

    private static final String TAG = "UserProfileFragment";

    /* Tag to refer to this fragment */
    public static final String TAG_PROFILE_FRAGMENT = "UserProfileFragment";

    /* UI Handles */
    private SignInButton sSignInButton;
    private LinearLayout sProfileContainer;

    private UserProfileFragmentCallbacks userProfileFragmentCallbacks;

    /**
     * Listener interface for sign-in click events. Activities hosting this
     * {@link UserProfileFragment} must implement this
     */
    public static interface UserProfileFragmentCallbacks {

        /**
         * Called when the user clicks {@link com.google.android.gms.common.SignInButton}
         * of this fragment.
         */
        void onSignInClicked();
    }

    public static UserProfileFragment getUserProfileFragment(
            FragmentActivity activity) {
        if (!(activity instanceof UserProfileFragmentCallbacks)) {
            throw new IllegalArgumentException(
                    "The activity must implement UserProfileFragmentCallbacks" +
                            " to receive callbacks.");
        }

        // Check if the fragment is already attached
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_PROFILE_FRAGMENT);
        if (fragment instanceof UserProfileFragment) {
            return (UserProfileFragment) fragment;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // If fragment was already attached to clean it up
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        // Create new bundle and attach it to the fragment manager
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        fragmentTransaction.add(userProfileFragment, TAG_PROFILE_FRAGMENT);
        fragmentTransaction.commit();
        return userProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        View externalLayout = rootView.findViewById(R.id.sign_in_button_container);
        sProfileContainer = (LinearLayout) rootView.findViewById(R.id.profile_container);
        sSignInButton = (SignInButton) externalLayout.findViewById(R.id.sign_in_button);
        sSignInButton.setSize(SignInButton.SIZE_WIDE);
        sSignInButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            userProfileFragmentCallbacks = (UserProfileFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The activity must implement UserProfileFragmentCallbacks " +
                            "to receive callbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        userProfileFragmentCallbacks = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                if (userProfileFragmentCallbacks != null) {
                    Log.i(TAG, "sign in clicked");
                    userProfileFragmentCallbacks.onSignInClicked();
                }
                break;
        }
    }

    public void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            /* Load User Profile */
            sProfileContainer.setVisibility(View.VISIBLE);
            sSignInButton.setVisibility(View.INVISIBLE);

        } else {
            /* Show sign in button */
            sProfileContainer.setVisibility(View.INVISIBLE);
            sSignInButton.setVisibility(View.VISIBLE);

        }
    }
}
