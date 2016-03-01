package org.androidcru.crucentralcoast;

/**
 * Used to store things like keys into various databases such as bundles,
 * disk caches, SharedPreferences.
 *
 * Used to store static strings such as regex expressions
 */
public class AppConstants
{
    /**
     * KEYS
     */

    public static final String FIRST_LAUNCH = "first_launch";

    //GCM
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PLAY_SERVICES = "play_services";

    //Notifications
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    //Facebook integration
    public static final String FB_TOKEN_KEY = "fb_token_key";
    public static final String EVENT_ID = "event_id";

    //RideSharing intent keys
    public static final String RIDE_KEY = "filled ride";


    /**
     * REGEX EXPRESSIONS & FORMATTERS
     */

    //lol don't ask. SO is God. http://stackoverflow.com/a/124179/1822968
    public static final String PHONE_REGEX = "1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?" +
            "\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\se?x?t?(\\d*))?";

    public final static String DATE_FORMATTER = "EEEE MMMM dd,";
    public final static String TIME_FORMATTER = "h:mm a";

    /**
     * MAGIC NUMBERS
     */

    public static final double CALPOLY_LAT = 35.30021;
    public static final double CALPOLY_LNG = -120.66310;
}
