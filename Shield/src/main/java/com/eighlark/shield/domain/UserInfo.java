package com.eighlark.shield.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.eighlark.shield.R;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   3/11/13
 */
public class UserInfo {

    private static final String PREF_USER_NAME =
            "com.eighlark.shield.info.PREF_USER_NAME";

    private static final String PREF_EMAIL_ID =
            "com.eighlark.shield.info.PREF_EMAIL_ID";

    private static final String PREF_APP_VERSION =
            "com.eighlark.shield.info.PREF_APP_VERSION";

    private static final String PREF_INFO_SAVED =
            "com.eighlark.shield.info.PREF_INFO_SAVED";

    private Context context;
    private SharedPreferences sharedPreferences;

    private String USER_NAME;
    private String EMAIL_ID;
    private int APP_VERSION;

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public int getAPP_VERSION() {
        return APP_VERSION;
    }

    /**
     * Info Constructor
     * @param context, Application context passed in calling activity
     * @throws IllegalArgumentException, if context equals null
     */
    public UserInfo(Context context) throws IllegalArgumentException {
        if (context == null) throw
                new IllegalArgumentException("Application Context can not be null");

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);

        // If information is already saved, retrieve it.
        if (exists()) {
            this.USER_NAME = sharedPreferences.getString(
                    PREF_USER_NAME, context.getString(R.string.PREF_USER_NAME_DEFAULT));
            this.EMAIL_ID = sharedPreferences.getString(
                    PREF_EMAIL_ID, context.getString(R.string.PREF_EMAIL_ID_DEFAULT));
            this.APP_VERSION = sharedPreferences.getInt(
                    PREF_APP_VERSION,
                    Integer.valueOf(context.getString(R.string.PREF_APP_VERSION_DEFAULT)));
        } else {
            try {
                PackageInfo packageInfo = this.context.getPackageManager()
                        .getPackageInfo(this.context.getPackageName(), 0);
                this.APP_VERSION = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                // should never happen
                throw new RuntimeException("Could not get package name: " + e);
            }
        }
    }

    /**
     * Checks if the app information has been saved.
     * @return true if app information is stored in shared preference.
     */
    public boolean exists() {
        return sharedPreferences.getBoolean(PREF_INFO_SAVED, false);
    }

    /**
     * Method to persist app information into shared preferences
     */
    public void save() throws IllegalStateException {
        if (this.USER_NAME == null)
            throw new IllegalStateException(
                    "Set USER_NAME using Info.setUserName() before calling Info.save()");
        if (this.EMAIL_ID == null)
            throw new IllegalStateException(
                    "Set EMAIL_ID using Info.setEmailId() before calling Info.save()");
        if (this.APP_VERSION <= 0)
            throw new IllegalArgumentException("Illegal value of Info.APP_VERSION");

        SharedPreferences.Editor sEditor = sharedPreferences.edit();

        sEditor.putString(PREF_USER_NAME, this.USER_NAME);
        sEditor.putString(PREF_EMAIL_ID, this.EMAIL_ID);
        sEditor.putInt(PREF_APP_VERSION, this.APP_VERSION);

        // Sets flag reflecting app information has been saved.
        sEditor.putBoolean(PREF_INFO_SAVED, true);

        sEditor.commit();
    }
}