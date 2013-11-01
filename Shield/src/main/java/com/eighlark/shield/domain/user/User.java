package com.eighlark.shield.domain.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */
public class User {

    private Context context;
    private String fName;
    private String emailId;
    private String gcmId;

    private UserDBHelper userDBHelper = new UserDBHelper(context);

    // Used for writing to the User Database
    private SQLiteDatabase writeDB = userDBHelper.getWritableDatabase();

    // Used for reading from the User Database
    private SQLiteDatabase readDB = userDBHelper.getReadableDatabase();

    // New map of values where, column names are the keys
    ContentValues contentValues = new ContentValues();

    /**
     * Constructor
     * @param context, needs to be instantiated from within an activity.
     */
    public User(Context context) {
        this.context = context;
    }

    /**
     * User Constructor with Default Table Schema as params.
     * @param context, {@link android.content.Context}
     * @param fName, First Name retrieved from G+ Profile.
     * @param emailId, Email ID retrieved from G+ Profile.
     * @param gcmId, retrieved from Google Cloud Messaging Server. Can be NULL.
     * @throws java.lang.IllegalArgumentException, null argument.
     */
    public User(Context context, String fName, String emailId, String gcmId)
            throws IllegalArgumentException {
        if (context == null) throw new IllegalArgumentException("Requires Application Context");
        if (fName == null) throw new IllegalArgumentException("Requires First Name");
        if (emailId == null) throw new IllegalArgumentException("Requires Email Id");
        if (gcmId == null) throw new IllegalArgumentException("Requires GCM Id");

        this.context = context;
        this.fName = fName;
        this.emailId = emailId;
        this.gcmId = gcmId;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    /**
     * @return rowId, after inserting data into User Table
     */
    private long saveUser() {
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_FNAME, fName);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_EMAIL_ID, emailId);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_GCMID, gcmId);

        return writeDB.insert(
                UserContract.UserEntry.TABLE_NAME,
                UserContract.UserEntry.COLUMN_NAME_NULLABLE,
                contentValues);
    }
}
