package org.androidcru.crucentralcoast.util;

import android.util.Log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import timber.log.Timber;

public class PrettyDebugTree extends Timber.Tree
{
    public PrettyDebugTree() {
        Logger.init("PrettyLogger")
                .logLevel(LogLevel.FULL)
                .methodOffset(5);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        switch (priority) {
            case Log.VERBOSE:
                Log.v(tag, message);
                break;
            case Log.DEBUG:
                Logger.d(message);
                break;
            case Log.INFO:
                Logger.i(message);
                break;
            case Log.WARN:
                Logger.w(message);
                break;
            case Log.ERROR:
                Logger.e(t, message);
                break;
            case Log.ASSERT:
                Logger.wtf(message);
                break;
        }

    }
}
