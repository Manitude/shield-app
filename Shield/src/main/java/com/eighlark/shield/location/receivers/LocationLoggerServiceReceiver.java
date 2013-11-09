package com.eighlark.shield.location.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.eighlark.shield.location.services.BackgroundLocationService;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   9/11/13
 */
public class LocationLoggerServiceReceiver extends BroadcastReceiver {

    private SharedPreferences mPrefs;
    public static final String TAG = "LocationLoggerServiceManager";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "Receiver started");
        if (isMyServiceRunning(context)) {

            Log.i(TAG, "Location Tracking Service already running");
        } else {

            /*
             * Get any previous setting for location updates
             * Gets "false" if an error occurs
             */
            ComponentName comp =
                    new ComponentName(
                            context.getPackageName(),
                            BackgroundLocationService.class.getName());

            context.startService(new Intent().setComponent(comp));
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
