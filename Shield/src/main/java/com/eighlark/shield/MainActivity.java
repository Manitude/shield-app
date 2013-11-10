package com.eighlark.shield;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.eighlark.shield.fragments.NavigationDrawerFragment;
import com.eighlark.shield.fragments.PlaceholderFragment;
import com.eighlark.shield.fragments.ShieldMapFragment;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends ActionBarActivity
        implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment sNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence sTitle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    /**
     * Application First Run Flag
     */
    private boolean FIRST_RUN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (sNavigationDrawerFragment == null)
            sNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        sTitle = getTitle();

        // Set up the drawer.
        sNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Informs Parse Push Service that this class will handle all Push Notifications
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Intent to start Location Tracking Services
        Intent broadcastIntent = new Intent(getString(R.string.location_service_intent));
        this.sendBroadcast(broadcastIntent);

        // If the app is running for the first time setup the default preferences
        setupPreferences();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                new ShieldMapFragment();
                Fragment shieldMapFragment = ShieldMapFragment.newInstance(position + 1);
                shieldMapFragment.getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, shieldMapFragment)
                        .commit();
                break;
            default:
                new PlaceholderFragment();
                Fragment placeholderFragment = PlaceholderFragment.newInstance(position + 1);
                placeholderFragment.getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, placeholderFragment)
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                sTitle = getString(R.string.title_section_map);
                break;
            case 2:
                sTitle = getString(R.string.title_section2);
                break;
            case 3:
                sTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(sTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (sNavigationDrawerFragment == null)
            sNavigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (!sNavigationDrawerFragment.isDrawerOpen()) {
            /**
             * Only show items in the action bar relevant to this screen
             * if the drawer is not showing. Otherwise, let the drawer
             * decide what to show in the action bar.
             */
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as a parent activity is specified in AndroidManifest.xml.
         */
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        FIRST_RUN = sharedPreferences
                .getBoolean(getString(R.string.PREF_FIRST_RUN), true);

        if (FIRST_RUN) {
            sharedPreferencesEditor.putBoolean(getString(R.string.PREF_FIRST_RUN), false);
            sharedPreferencesEditor.commit();

            // Initialize Default values of preferences on first run
            PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
            PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);
            PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);
        }
    }

}