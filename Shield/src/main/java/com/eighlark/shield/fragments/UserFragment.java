package com.eighlark.shield.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eighlark.shield.R;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   16/11/13
 */
public class UserFragment extends Fragment {

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        return rootView;
    }
}
