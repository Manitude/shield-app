package com.eighlark.shield.location.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eighlark.shield.R;
import com.eighlark.shield.location.services.BackgroundLocationService;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   9/11/13
 */
public class LocationLoggerServiceReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;
    public static final String TAG = "LocationLoggerServiceManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ComponentName componentName = new ComponentName(context.getPackageName(),
                BackgroundLocationService.class.getName());

        Log.i(TAG, "Receiver started");

        // Start/Stop location monitoring service based on user preference
        if (sharedPreferences.getBoolean(context.getString(R.string.PREF_KEY_LOC_SERVICE), true)) {
            if (isMyServiceRunning(context)) {

                Log.i(TAG, "Location Tracking Service already running");
            } else {

                /*
                 * Get any previous setting for location updates
                 * Gets "false" if an error occurs
                 */
                context.startService(new Intent().setComponent(componentName));
            }
        } else {

            context.stopService(new Intent().setComponent(componentName));
        }

    }

    private boolean isMyServiceRunning(Context context) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (
                ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundLocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
