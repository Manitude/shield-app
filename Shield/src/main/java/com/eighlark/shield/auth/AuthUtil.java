package com.eighlark.shield.auth;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eighlark.shield.model.User;
import com.google.android.gms.common.Scopes;

import java.net.HttpURLConnection;

/**
 * Provides static utility methods to help make authenticated requests.
 */
public class AuthUtil {

    private static final String TAG = AuthUtil.class.getSimpleName();

    public static final String[] SCOPES = {
            Scopes.PLUS_LOGIN
    };

    public static final String[] VISIBLE_ACTIVITIES = {
            "http://schemas.google.com/AddActivity", "http://schemas.google.com/ReviewActivity"
    };

    private static final String SCOPE_STRING = "oauth2:" + TextUtils.join(" ", SCOPES);

    private static final String ACCESS_TOKEN_JSON = "{ \"access_token\":\"%s\"}";

    private static String sAccessToken = null;

    private static String sCookies = null;

    public static void setAuthHeaders(HttpURLConnection connection) {
        Log.d(TAG, "Authorization: OAuth " + sAccessToken);
        connection.setRequestProperty("Authorization", "OAuth " + sAccessToken);
        connection.setRequestProperty("Cookie", sCookies);
    }

    public static User authenticate(Context ctx, String account) {
        //TODO retrieve user from Parse based on Google+ account name
        User user = new User("sdfsdfsd", account, "asdasd", "Aasdasd");
        return user;
    }

    public static void invalidateSession() {

    }
}
