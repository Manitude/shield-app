package com.eighlark.shield;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eighlark.shield.fragments.ErrorDialogFragment;
import com.eighlark.shield.fragments.NavigationDrawerFragment;
import com.eighlark.shield.location.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends BaseActivity
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment sNavigationDrawerFragment;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private FragmentManager sFragmentManager;
    private SupportMapFragment sSupportMapFragment;
    private GoogleMap sGoogleMap;

    private LocationClient sLocationClient;
    private LocationRequest sLocationRequest;
    private Marker currentLocationMarker;
    private CameraUpdate sCameraUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (sNavigationDrawerFragment == null)
            sNavigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        sNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Update the main content by replacing with Map Fragment
        sFragmentManager = getSupportFragmentManager();
        sSupportMapFragment = (SupportMapFragment) sFragmentManager.findFragmentById(R.id.map);

        // Create the LocationRequest object
        sLocationRequest = LocationRequest.create();
        // Use high accuracy
        sLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        sLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        sLocationRequest.setFastestInterval(LocationUtils.FASTEST_INTERVAL);

        // Trace the Google Map on UI
        setUpMapIfNeeded();

        // Informs Parse Push Service that this class will handle all Push Notifications
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // If the app is running for the first time setup the default preferences
        setupPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Instantiate Location Client in order to track current location
        setUpLocationClientIfNeeded();

        sLocationClient.connect();

        // Trace the Google Map on UI
        setUpMapIfNeeded();

        // Enable/Disable Location monitoring service based on user preference
        setupLocationMonitoring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sLocationClient != null) {
            sLocationClient.disconnect();
        }
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
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            sCameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
            // TODO Create custom location marker by retrieving profile picture of user from Google+
            if (currentLocationMarker != null) {
                currentLocationMarker.setPosition(currentLocation);

            } else {
                // Add current location marker to map
                currentLocationMarker = sGoogleMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .title(getString(R.string.marker_current_location)));

                // Setup marker and move camera to current marker location
                sGoogleMap.animateCamera(sCameraUpdate);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /**
         * If the error has a resolution, start a Google Play services
         * activity to resolve it.
         */
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
            // If no resolution is available, display an error dialog
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(), "Shield");
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
                Toast.makeText(this, "Loading map...", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNavigationDrawerViewClicked(View view) {
        Toast.makeText(this, "view clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            if (sNavigationDrawerFragment.isDrawerOpen()) {
                /** Hide drawer */
                sNavigationDrawerFragment.hideDrawer();
            } else {
                /** Opens Drawer onMenuButton click event */
                sNavigationDrawerFragment.showDrawer();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void OnMyLocationClicked(View view) {
        sGoogleMap.animateCamera(sCameraUpdate);
    }

    private void setUpMapIfNeeded() {
        if (sSupportMapFragment == null) {
            sSupportMapFragment = SupportMapFragment.newInstance();
        } else {
            sGoogleMap = sSupportMapFragment.getMap();
            sGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    private void setUpLocationClientIfNeeded() {
        if (sLocationClient == null) {
            sLocationClient = new LocationClient(
                    this,
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }

    private void setupPreferences() {
        // Initialize Default values of preferences on first run
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_services, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);
    }

    private void setupLocationMonitoring() {
        // Intent to start Location Tracking Services
        Intent broadcastIntent = new Intent(getString(R.string.location_service_intent));
        this.sendBroadcast(broadcastIntent);
    }
}