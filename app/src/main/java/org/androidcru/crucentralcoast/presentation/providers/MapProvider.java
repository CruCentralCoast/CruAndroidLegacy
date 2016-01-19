package org.androidcru.crucentralcoast.presentation.providers;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.util.Pair;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Location;
import org.threeten.bp.DateTimeUtils;

import java.util.Calendar;
import java.util.Locale;

import rx.Observable;
import rx.Observer;

public final class MapProvider
{
    private static MapProvider mInstance;

    private MapProvider()
    {
    }

    public static MapProvider getInstance()
    {
        if (mInstance == null)
            mInstance = new MapProvider();
        return mInstance;
    }

    public void openMap(CruEvent cruEvent)
    {
        Location  loc = cruEvent.mLocation;
        String uri = String.format(Locale.ENGLISH, "https://www.google.com/maps/place/%s", loc.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CruApplication.getContext().startActivity(intent);
    }
}
