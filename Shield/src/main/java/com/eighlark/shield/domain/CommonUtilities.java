package com.eighlark.shield.domain;

/**
 * Created at Eighlark Innovations.
 * Author: Akshay
 * Date:   6/11/13
 */
public final class CommonUtilities {

    // For Google API Key reference
    public static final String SENDER_ID = "825002230196";

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 30;

    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 30;

    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    // Stores the lat / long pairs in a text file
    public static final String LOCATION_TAG = "ShieldLocation";

    // Stores the connect / disconnect data in a text file
    public static final String LOG_TAG = "ShieldLog";

    /**
     * Suppress default constructor
     */
    private CommonUtilities() {
        throw new AssertionError();
    }
}