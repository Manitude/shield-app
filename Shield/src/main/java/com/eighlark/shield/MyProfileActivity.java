package com.eighlark.shield;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eighlark.shield.fragments.MyProfileFragment;
import com.eighlark.shield.model.User;
import com.google.android.gms.common.SignInButton;

public class MyProfileActivity extends BaseActivity
        implements
        View.OnClickListener {

    private static final String TAG = "MyProfileActivity";

    private SignInButton sSignInButton;
    private RelativeLayout containerRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Check first if the user is logged into Google+
        if (!isAuthenticated() && !isAuthenticating()) {
            // Show Sign In button if not logged in
            setupSignInButton();

            containerRelativeLayout = (RelativeLayout) findViewById(R.id.container);
            containerRelativeLayout.addView(sSignInButton);
            
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MyProfileFragment())
                    .commit();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void onSetAuthenticatedProfile(User profile) {
        super.onSetAuthenticatedProfile(profile);
    }

    @Override
    public void update() {
        super.update();
        if (sShieldUser != null) {
            containerRelativeLayout.removeView(sSignInButton);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MyProfileFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupSignInButton() {
        // Setup layout params
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        // Draw {@link sSignInButton}
        sSignInButton = (SignInButton) getLayoutInflater().inflate(
                R.layout.sign_in_button, null);
        sSignInButton.setLayoutParams(layoutParams);
        sSignInButton.setSize(SignInButton.SIZE_WIDE);
        sSignInButton.setOnClickListener(this);
    }
}
