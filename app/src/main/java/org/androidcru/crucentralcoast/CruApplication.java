package org.androidcru.crucentralcoast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.anupcowkur.reservoir.Reservoir;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.aaronhe.threetengson.ThreeTenGsonAdapter;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;

public class CruApplication extends MultiDexApplication
{


    public static Gson gson;

    private static Context context;

    private static String PACKAGE_NAME;

    public static Context getContext()
    {
        return context;
    }
    public static String retrievePackageName()
    {
        return PACKAGE_NAME;
    }
    public static final String FB_TOKEN_KEY = "fb_token_key";
    public static final String FIRST_LAUNCH = "first_launch";

    private static SharedPreferences sharedPreferences;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PLAY_SERVICES = "play_services";


    @Override
    public void onCreate()
    {
        super.onCreate();

        Logger.init().setLogLevel(LogLevel.values()[BuildConfig.LOG_LEVEL]);

        context = this;
        PACKAGE_NAME = getPackageName();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        FacebookSdk.sdkInitialize(context);

        setupDiskCache();
        setupGson();

        if (checkPlayServices())
        {
            // Start IntentService to register this application with GCM.
            Intent service = new Intent(this, RegistrationIntentService.class);
            startService(service);
        }
    }

    /**
     * Important because it's essential that SharedPreferences doesn't get overwritten
     * @return
     */
    public static SharedPreferences getSharedPreferences()
    {
        return sharedPreferences;
    }


    /**
     * Determines if the current device is enabled for google play services and if not, will prompt
     * the user to enable it.
     *
     * @return true if enabled, otherwise false
     */
    private boolean checkPlayServices()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        sharedPreferences.edit().putInt(PLAY_SERVICES, resultCode).apply();
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void setupDiskCache()
    {
        try
        {
            Reservoir.init(this, 10240, gson); //in bytes
        } catch (Exception e)
        {
            Logger.e(e, "Not enough space for disk cache!");
        }
    }

    private void setupGson()
    {
        GsonBuilder builder = new GsonBuilder();
        builder = ThreeTenGsonAdapter.registerAll(builder);
        gson = builder.create();
    }

    public static void saveGCMKey(String key)
    {
        CruApplication.getSharedPreferences().edit().putString(context.getString(R.string.gcm_registration_id), key).apply();
    }

    public static String getGCMKey()
    {
        return CruApplication.getSharedPreferences().getString(context.getString(R.string.gcm_registration_id), "");
    }

}
