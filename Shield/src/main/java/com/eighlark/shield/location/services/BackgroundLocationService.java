package com.eighlark.shield.location.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.eighlark.shield.MainActivity;
import com.eighlark.shield.R;
import com.eighlark.shield.domain.CommonUtilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   8/11/13
 */
public class BackgroundLocationService extends Service
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "BackgroundLocationService";
    private static final int sNOTIFICATION_ID = 1;

    IBinder sBinder = new LocalBinder();

    private LocationClient sLocationClient;
    private LocationRequest sLocationRequest;

    // Notification Handles
    private NotificationCompat.Builder sNotificationBuilder;
    private Intent resultIntent;
    private PendingIntent resultPendingIntent;
    private Notification sNotification;
    private NotificationManager sNotificationManager;

    public class LocalBinder extends Binder {
        public BackgroundLocationService getServerInstance() {
            return BackgroundLocationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the LocationRequest object
        sLocationRequest = LocationRequest.create();

        // Use high accuracy
        sLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Set the update interval to 5 seconds
        sLocationRequest.setInterval(CommonUtilities.UPDATE_INTERVAL);

        // Set the fastest update interval to 1 second
        sLocationRequest.setFastestInterval(CommonUtilities.FASTEST_INTERVAL);

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        sLocationClient = new LocationClient(this, this, this);
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i(TAG, "Service Started");

        if (!servicesAvailable() || sLocationClient.isConnected())
            return START_STICKY;

        setUpLocationClientIfNeeded();

        if (!sLocationClient.isConnected() || !sLocationClient.isConnecting()) {
            sLocationClient.connect();
        }

        setUpNotification();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }

    @Override
    public void onDestroy(){

        if(servicesAvailable() && sLocationClient != null) {
            sLocationClient.removeLocationUpdates(this);
            // Destroy the current location client
            sLocationClient = null;
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        sLocationClient.requestLocationUpdates(sLocationRequest, this);  // LocationListener;
    }

    @Override
    public void onDisconnected() {
        // Destroy the current Location Client
        sLocationClient = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.i(TAG,
                    String.format("%s, %s",
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude())));
            if (sNotificationBuilder != null) {
                sNotificationBuilder
                        .setContentText(
                                String.format("%s, %s",
                                        String.valueOf(location.getLatitude()),
                                        String.valueOf(location.getLongitude())));

                sNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                sNotificationManager.notify(
                        sNOTIFICATION_ID,
                        sNotificationBuilder.build());
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
         /*
          * Google Play services can resolve some errors it detects.
          * If the error has a resolution, try sending an Intent to
          * start a Google Play services activity that can resolve
          * error.
          */
        if (connectionResult.hasResolution()) {

            // If no resolution is available, display an error dialog
        } else {

        }
    }

    private boolean servicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            return true;
        } else {

            return false;
        }
    }

    private void setUpNotification() {

        sNotificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.notification_location_title))
                        .setTicker("Shield has started")
                        .setContentText(getString(R.string.notification_location_content))
                        .setAutoCancel(false);

        resultIntent = new Intent(this, MainActivity.class);

        resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        sNotificationBuilder.setContentIntent(resultPendingIntent);
        sNotification = sNotificationBuilder.build();
        startForeground(sNOTIFICATION_ID, sNotification);
    }

    private void setUpLocationClientIfNeeded() {
        if (sLocationClient == null) {
            sLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
        sLocationClient.connect();
    }

}