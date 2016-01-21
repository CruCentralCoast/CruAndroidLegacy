package org.androidcru.crucentralcoast;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.aaronhe.threetengson.ThreeTenGsonAdapter;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;

import java.util.ArrayList;

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


    @Override
    public void onCreate()
    {
        super.onCreate();
        Logger.init().setLogLevel(LogLevel.values()[BuildConfig.LOG_LEVEL]);
        context = this;
        PACKAGE_NAME = context.getPackageName();
        GsonBuilder builder = new GsonBuilder();
        builder = ThreeTenGsonAdapter.registerAll(builder);
        gson = builder.create();

        try {
            Reservoir.init(this, 10240, gson); //in bytes
        } catch (Exception e) {
            Logger.e(e,"Not enough space for disk cache!");
        }
    }


}
