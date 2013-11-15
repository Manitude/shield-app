package com.eighlark.shield.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Wraps the {@link android.app.Dialog} returned by
 * {@link com.google.android.gms.common.GooglePlayServicesUtil#getErrorDialog} so that it can be properly
 * managed by the {@link android.app.Activity}.
 */
public class GooglePlayServicesErrorDialogFragment extends DialogFragment {

    /**
     * The error code returned by the
     * {@link com.google.android.gms.common.GooglePlayServicesUtil#isGooglePlayServicesAvailable(android.content.Context)}
     * call.
     */
    public static final String ARG_ERROR_CODE = "errorCode";

    /**
     * The request code given when calling
     * {@link android.app.Activity#startActivityForResult}.
     */
    public static final String ARG_REQUEST_CODE = "requestCode";

    /**
     * Returns a {@link android.app.Dialog} created by
     * {@link com.google.android.gms.common.GooglePlayServicesUtil#getErrorDialog} with the provided
     * errorCode, activity, and request code.
     * 
     * @param savedInstanceState Not used.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        return GooglePlayServicesUtil.getErrorDialog(args.getInt(ARG_ERROR_CODE), getActivity(),
                args.getInt(ARG_REQUEST_CODE));
    }

    /**
     * Create a {@link android.support.v4.app.DialogFragment} for displaying the
     * {@link com.google.android.gms.common.GooglePlayServicesUtil#getErrorDialog}.
     * 
     * @param errorCode The error code returned by
     *            {@link com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener}
     * @param requestCode The request code for resolving the resolution
     *            activity.
     * @return The {@link android.support.v4.app.DialogFragment}.
     */
    public static Bundle createArguments(int errorCode, int requestCode) {
        Bundle args = new Bundle();
        args.putInt(GooglePlayServicesErrorDialogFragment.ARG_ERROR_CODE, errorCode);
        args.putInt(GooglePlayServicesErrorDialogFragment.ARG_REQUEST_CODE, requestCode);
        return args;
    }
}
