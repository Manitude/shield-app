package com.eighlark.shield.domain.user;

import android.provider.BaseColumns;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   1/11/13
 */
public final class UserContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // Create table Command with schema defined {@link UserEntry}.
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_FNAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_EMAIL_ID + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_GCMID + TEXT_TYPE + COMMA_SEP +
                    " )";

    // Delete Table Command, If exists in database.
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public UserContract() {
    }

    // Inner class that defines the table schema
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_EMAIL_ID = "emailId";
        public static final String COLUMN_NAME_FNAME = "fName";
        public static final String COLUMN_NAME_GCMID = "gcmId";
        public static final String COLUMN_NAME_NULLABLE = "null";
    }

}
