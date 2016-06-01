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
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PHONE_NUMBER = "user_phone_number";

    //GCM
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PLAY_SERVICES = "play_services";
    public static final String GCM_REGISTRATION_ID = "gcm_registration_id";

    //Events
    public static final int DRIVER_REQUEST_CODE = 7;
    public static final int PASSENGER_REQUEST_CODE = 9;

    //MyRides Tabs
    public static final String MY_RIDES_TAB = "MY_RIDES_TAB";
    public static final int PASSENGER_TAB = 0;
    public static final int DRIVER_TAB = 1;

    //Notifications
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    //Facebook integration
    public static final String FB_TOKEN_KEY = "fb_token_key";
    public static final String EVENT_ID = "event_id";

    //RideSharing intent keys
    public static final String RIDE_KEY = "filled ride";
    public static final String MYRIDE_RIDE_KEY = "my ride ride";
    public static final String EVENT_KEY = "event";

    public static final String EVENT_STARTDATE = "event_startdate";

    public static final String DATE_yyyyMMdd = "yyyy-MM-dd";
    public static final String DATE_DISPLAY_PATTERN = "MMM dd, yyyy";
    public static final String TIME_PARSE = "HH:mm:ss";
    public static final String TIME_FORMAT = "h:mm a";

    public static final String INIT_DIRECTION_SPINNER_OPTION = "Select Direction";
    public static final String INIT_GENDER_SPINNER_OPTION = "Select Gender";
    public static final String AUTOCOMPLETE_HINT = "Pickup Location";

    public static final String SUPER_SPECIAL_STRING = "whatever";

    //Ministry Team
    public static final String MINISTRY_TEAM_KEY = "MINISTRY_TEAM";

    //Community Groups
    public static final String COMMUNITY_GROUP = "COMMUNITY_GROUP";

    //Login
    public static final String LOGIN_KEY = "LOGIN_KEY";
    public static final String USERNAME_KEY = "USERNAME_KEY";

    //Notification
    public static final String NOTIFICATION_KEY = "Notification_KEY";

    //Authorized Driver
    public static final String AUTHORIZED_KEY = "AUTHORIZED_KEY";

    /**
     * REGEX EXPRESSIONS & FORMATTERS
     */

    //lol don't ask. SO is God. http://stackoverflow.com/a/124179/1822968
    public static final String PHONE_REGEX = "1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?" +
            "\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\se?x?t?(\\d*))?";

    public final static String DATE_FORMATTER = "EEEE MMMM dd,";
    public final static String DATE_FORMATTER_NO_DAY = "MMMM d, YYYY";
    public final static String TIME_FORMATTER = "h:mm a";

    /**
     * MAGIC NUMBERS
     */

    public static final double CALPOLY_LAT = 35.30021;
    public static final double CALPOLY_LNG = -120.66310;

    public static final String CRU_YOUTUBE_CHANNEL_ID = "UCe-RJ-3Q3tUqJciItiZmjdg";
    public static final String CRU_YOUTUBE_UPLOADS_ID = "UUe-RJ-3Q3tUqJciItiZmjdg";
    public static final String EXPANDED = "Hide Description";
    public static final String RETRACTED = "Show Description";
    public static final String VIDEO_PLAY_FAILED_MESSAGE = "Unable to play video";
    public static final long PAGE_SIZE = 20l;
    public static final String SPACE_COMMA_ESCAPE = "\\s*,\\s*";

    public static final int RETRY_ATTEMPTS = 5;
    
    public static final float RADIUS_STROKE_WID = 5;
    public static final int MAX_CAR_CAPACITY = 20;
    public static final double MAX_RADIUS = 25.0;
    public static final String FREIG_SAN_PRO_LIGHT = "FreigSanProLig.otf";
}
