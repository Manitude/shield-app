package com.eighlark.shield;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.testflightapp.lib.TestFlight;

public class Shield extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TestFlight.takeOff(this, "9dcc57b4-6e07-4c7e-88bb-a2a8782db7fd");

        // Initialize Parse Library
        Parse.initialize(this,
                "KQwLyizN30RXqIGE7jXqbgdeRf8WnpqPUd84ua9E" /* APP ID */,
                "WfLNlgXJC3OfZ45FenhoGbHtrboLmrIhsrTfDpXv" /* Client Key */);

        ParseFacebookUtils.initialize("226605250848315");
    }
}
