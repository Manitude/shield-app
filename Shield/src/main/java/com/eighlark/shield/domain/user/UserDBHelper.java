package com.eighlark.shield.domain.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   1/11/13
 */
public class UserDBHelper extends SQLiteOpenHelper {

    // Increment Version number, in case the schema of DB is changed.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UserContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /**
         * This database is only a cache for online data, so its upgrade policy is
         * to simply discard the data and start over.
         */
        sqLiteDatabase.execSQL(UserContract.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }

    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}
