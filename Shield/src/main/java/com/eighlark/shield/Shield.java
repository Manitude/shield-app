package com.eighlark.shield;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date: 1/11/13
 */
import android.app.Application;

import com.testflightapp.lib.TestFlight;

public class Shield extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TestFlight.takeOff(this, "9dcc57b4-6e07-4c7e-88bb-a2a8782db7fd");
    }
}
