package com.eighlark.shield;

import android.os.Bundle;

import com.eighlark.shield.fragments.AddPersonFragment;

public class AddPersonActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddPersonFragment())
                    .commit();
        }
    }

}
