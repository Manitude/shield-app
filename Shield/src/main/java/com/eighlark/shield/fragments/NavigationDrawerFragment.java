package com.eighlark.shield.fragments;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eighlark.shield.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks sCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle sDrawerToggle;

    private DrawerLayout sDrawerLayout;
    private ListView sDrawerListView;
    private LinearLayout sDrawerCustomView;
    private View sFragmentContainerView;

    private int sCurrentSelectedPosition = 0;
    private boolean sFromSavedInstanceState;
    private boolean sUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Read in the flag indicating whether or not the user has demonstrated awareness of the
         * drawer. See PREF_USER_LEARNED_DRAWER for details.
         */
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUserLearnedDrawer = sharedPreferences.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            sCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            sFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        /** Initialize the Navigation drawer layout */
        sDrawerCustomView = (LinearLayout) inflater.inflate(
                R.layout.fragment_nav_drawer, container, false);
        sDrawerCustomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem(view);
            }
        });

        /** Initializing the Navigation Drawer option list */
        sDrawerListView = (ListView) sDrawerCustomView.findViewById(R.id.drawer_list_view);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.nav_drawer_list_view_item,
                new String[]{
                        getString(R.string.title_settings),
                        getString(R.string.title_help),
                        getString(R.string.title_send_feedback)});
        sDrawerListView.setAdapter(stringArrayAdapter);
        sDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        sDrawerListView.setItemChecked(sCurrentSelectedPosition, true);

        return sDrawerCustomView;
    }

    public boolean isDrawerOpen() {
        return sDrawerLayout != null && sDrawerLayout.isDrawerOpen(sFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        sFragmentContainerView = getActivity().findViewById(fragmentId);
        sDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        sDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        /**
         * ActionBarDrawerToggle ties together the proper interactions
         * between the navigation drawer and the action bar app icon.
         */
        sDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                sDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!sUserLearnedDrawer) {
                    /**
                     * The user manually opened the drawer; store this flag to prevent auto-showing
                     * the navigation drawer automatically in the future.
                     */
                    sUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer.
        if (!sUserLearnedDrawer && !sFromSavedInstanceState) {
            sDrawerLayout.openDrawer(sFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        sDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                sDrawerToggle.syncState();
            }
        });

        sDrawerLayout.setDrawerListener(sDrawerToggle);
    }

    private void selectItem(int position) {
        sCurrentSelectedPosition = position;
        if (sDrawerListView != null) {
            sDrawerListView.setItemChecked(position, true);
        }
        if (sDrawerLayout != null) {
            sDrawerLayout.closeDrawer(sFragmentContainerView);
        }
        if (sCallbacks != null) {
            sCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    private void clickedItem(View view) {
        if (sDrawerLayout != null) {
            sDrawerLayout.closeDrawer(sFragmentContainerView);
        }
        if (sCallbacks != null) {
            sCallbacks.onNavigationDrawerViewClicked(view);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            sCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt(STATE_SELECTED_POSITION, sCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        sDrawerToggle.onConfigurationChanged(newConfig);
    }

    /** Show drawer if not open onMenuButton click event */
    public void showDrawer() {
        sDrawerLayout.openDrawer(sFragmentContainerView);
    }

    /** Hide drawer if already open OnMenuButton click event */
    public void hideDrawer() {
        if (isDrawerOpen()) {
            sDrawerLayout.closeDrawer(sFragmentContainerView);
        }
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);

        /**
         * Called when a view in the navigation drawer is selected.
         */
        void onNavigationDrawerViewClicked(View view);
    }
}
