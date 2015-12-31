package org.androidcru.crucentralcoast;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.aaronhe.threetengson.ThreeTenGsonAdapter;

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
