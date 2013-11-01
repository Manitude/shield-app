package com.eighlark.shield.fragments;

/**
 * Created at Eighlark Innovations
 *
 * @author: Akshay
 * @date: 1/11/13
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eighlark.shield.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashFragment extends Fragment {

    public SplashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        return rootView;
    }
}