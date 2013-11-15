/*
 * Created at Eighlark Innovation
 * Author: Akshay Kr Singh
 * Email: akshay.x666@gmail.com
 */
package com.eighlark.shield.location;

import android.content.Context;
import android.content.res.Resources;

import com.eighlark.shield.R;
import com.google.android.gms.common.ConnectionResult;

/**
 * Map error codes to error messages.
 */
public class LocationServiceErrorMessages {
    // Don't allow instantiation
    private LocationServiceErrorMessages () {}

    public static String getErrorString(Context context, int errorCode) {

        // Get a handle to resources, to allow the method to retrieve messages.
        Resources sResources = context.getResources();

        // Define a string to contain the error message
        String errorString;

        // Decide which error message to get, based on the error code.
        switch (errorCode) {

            case ConnectionResult.DEVELOPER_ERROR:
                errorString = sResources.getString(R.string.connection_error_misconfigured);
                break;

            case ConnectionResult.INTERNAL_ERROR:
                errorString = sResources.getString(R.string.connection_error_internal);
                break;

            case ConnectionResult.INVALID_ACCOUNT:
                errorString = sResources.getString(R.string.connection_error_invalid_account);
                break;

            case ConnectionResult.LICENSE_CHECK_FAILED:
                errorString = sResources.getString(R.string.connection_error_license_check_failed);
                break;

            case ConnectionResult.NETWORK_ERROR:
                errorString = sResources.getString(R.string.connection_error_network);
                break;

            case ConnectionResult.RESOLUTION_REQUIRED:
                errorString = sResources.getString(R.string.connection_error_needs_resolution);
                break;

            case ConnectionResult.SERVICE_DISABLED:
                errorString = sResources.getString(R.string.connection_error_disabled);
                break;

            case ConnectionResult.SERVICE_INVALID:
                errorString = sResources.getString(R.string.connection_error_invalid);
                break;

            case ConnectionResult.SERVICE_MISSING:
                errorString = sResources.getString(R.string.connection_error_missing);
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                errorString = sResources.getString(R.string.connection_error_outdated);
                break;

            case ConnectionResult.SIGN_IN_REQUIRED:
                errorString = sResources.getString(
                        R.string.connection_error_sign_in_required);
                break;

            default:
                errorString = sResources.getString(R.string.connection_error_unknown);
                break;
        }

        // Return the error message
        return errorString;
    }
}
