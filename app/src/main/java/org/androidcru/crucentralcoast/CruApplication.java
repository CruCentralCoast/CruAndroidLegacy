package org.androidcru.crucentralcoast;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class CruApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        Logger.init().setLogLevel(LogLevel.values()[BuildConfig.LOG_LEVEL]);
    }
}
