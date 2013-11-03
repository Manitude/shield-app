package com.eighlark.shield.fragments;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eighlark.shield.MainActivity;
import com.eighlark.shield.R;
import com.eighlark.shield.location.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.testflightapp.lib.TestFlight;

public class ShieldMapFragment extends Fragment
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap sMap;
    private static View rootView;

    private LocationClient sLocationClient;
    private LocationRequest sLocationRequest;
    private Marker currentLocationMarker;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ShieldMapFragment newInstance(int sectionNumber) {
        ShieldMapFragment fragment = new ShieldMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ShieldMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the LocationRequest object
        sLocationRequest = LocationRequest.create();
        // Use high accuracy
        sLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        sLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        sLocationRequest.setFastestInterval(LocationUtils.FASTEST_INTERVAL);

        // Test Flight Log
        TestFlight.log("Launched Successfully");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
            // Map is already there, just return the view as it is
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Instantiate Location Client in order to track current location
        setUpLocationClientIfNeeded();

        sLocationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sLocationClient != null) {
            sLocationClient.disconnect();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        sLocationClient.requestLocationUpdates(sLocationRequest, this);  // LocationListener;

        // Trace the Google Map on UI
        setUpMapIfNeeded();
    }

    @Override
    public void onDisconnected() {

        // Destroy the current Location Client
        sLocationClient = null;
    }

    /**
     * Implementation of {@link LocationListener}.
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // TODO Create custom location marker by retrieving profile picture of user from Google+
            currentLocationMarker.setPosition(currentLocation);

            // Test Flight Log
            TestFlight.log("Current Location: " + currentLocation.toString());
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
                        getActivity(),
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
                    getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getActivity().getSupportFragmentManager(), "Shield");
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMyLocationButtonClick() {

        // Test Flight Log
        TestFlight.log("My Location Button Clicked");

        // Test Flight Log
        TestFlight.log("Started Current Location Tracking");

        Toast.makeText(getActivity(),
                getString(R.string.location_trace_status), Toast.LENGTH_SHORT).show();
        /**
         * Return false so that we don't consume the event and the default behavior still occurs
         * (the camera animates to the user's current position).
         */
        return false;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the main.
        if (sMap == null) {
            // Try to obtain the main from the SupportMapFragment.

            // Test Flight Log
            TestFlight.log("Initiallizing Map");

            sMap = ((SupportMapFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (sMap != null) {

                // Test Flight Log
                TestFlight.log("Map Object Created Successfully");

                sMap.setMyLocationEnabled(true);
                sMap.setOnMyLocationButtonClickListener(this);
                sMap.setOnInfoWindowClickListener(this);
                try {
                    currentLocationMarker = sMap.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    sLocationClient.getLastLocation().getLatitude(),
                                    sLocationClient.getLastLocation().getLongitude()))
                            .title(getString(R.string.marker_current_location)));
                } catch (NullPointerException e) {
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
            }
        }
    }

    private void setUpLocationClientIfNeeded() {
        if (sLocationClient == null) {
            sLocationClient = new LocationClient(
                    getActivity().getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
    }
}
