package com.eighlark.shield.domain.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eighlark.shield.R;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   3/11/13
 */
public class User {

    private static final String PREF_NAME =
            "com.eighlark.shield.user.PREF_NAME";

    private static final String PREF_EMAIL =
            "com.eighlark.shield.user.PREF_EMAIL";

    private static final String PREF_GCM_ID =
            "com.eighlark.shield.user.PREF_GCM_ID";

    private static final String PREF_USER_EXISTS =
            "com.eighlark.shield.user.PREF_USER_EXISTS";

    private Context context;
    private SharedPreferences sharedPreferences;

    private String name;
    private String email;
    private String gcmId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    /**
     * User Constructor
     * @param context, Application context passed in calling activity
     * @throws IllegalArgumentException, if context equals null
     */
    public User(Context context) throws IllegalArgumentException {
        if (context == null) throw
                new IllegalArgumentException("Application Context can not be null");

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);

        // If user information is already saved, retrieve it.
        if (isSet()) {
            this.name = sharedPreferences.getString(
                    PREF_NAME, context.getString(R.string.PREF_NAME_DEFAULT));
            this.email = sharedPreferences.getString(
                    PREF_EMAIL, context.getString(R.string.PREF_EMAIL_DEFAULT));
            this.gcmId = sharedPreferences.getString(
                    PREF_GCM_ID, context.getString(R.string.PREF_GCM_ID_DEFAULT));
        }
    }

    /**
     * Checks if the user has been saved.
     * @return true if user information is stored in shared preference.
     */
    public boolean isSet() {
        return sharedPreferences.getBoolean(PREF_USER_EXISTS, false);
    }

    /**
     * Method to persist user information into shared preferences
     */
    public void save() throws IllegalStateException {
        if (this.name == null)
            throw new IllegalStateException("Set Name using setName() before calling save()");
        if (this.email == null)
            throw new IllegalStateException("Set Email using setEmail() before calling save()");
        if (this.gcmId == null)
            throw new IllegalStateException("Set GcmId using setGcmId() before calling save()");

        SharedPreferences.Editor sEditor = sharedPreferences.edit();

        sEditor.putString(PREF_NAME, this.name);
        sEditor.putString(PREF_EMAIL, this.email);
        sEditor.putString(PREF_GCM_ID, this.gcmId);

        // Sets flag reflecting user information has been saved.
        sEditor.putBoolean(PREF_USER_EXISTS, true);

        sEditor.commit();
    }
}