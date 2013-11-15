package com.eighlark.shield;

import android.os.Bundle;

import com.eighlark.shield.fragments.UserFragment;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new UserFragment())
                    .commit();
        }
    }

}
