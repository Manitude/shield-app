package com.eighlark.shield;

import android.os.Bundle;

import com.eighlark.shield.fragments.MyProfileFragment;

public class MyProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MyProfileFragment())
                    .commit();
        }
    }

}
