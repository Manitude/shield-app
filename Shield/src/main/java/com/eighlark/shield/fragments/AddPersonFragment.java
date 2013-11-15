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
public class AddPersonFragment extends Fragment {

    public AddPersonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_person, container, false);
        return rootView;
    }
}
