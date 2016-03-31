package org.androidcru.crucentralcoast.presentation.providers;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.util.Pair;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.threeten.bp.DateTimeUtils;

import java.util.Calendar;

import rx.Observable;
import rx.Observer;

//REVIEW singleton class that does not need to be singleton, static methods should do
public final class CalendarProvider
{
    private static CalendarProvider mInstance;

    private CalendarProvider()
    {
    }

    public static CalendarProvider getInstance()
    {
        if (mInstance == null)
            mInstance = new CalendarProvider();
        return mInstance;
    }

    public void addEventToCalendar(Context context, CruEvent cruEvent, Observer<Pair<String, Long>> parentSubscriber)
    {
        Calendar beginTime = DateTimeUtils.toGregorianCalendar(cruEvent.startDate);
        Calendar endTime = DateTimeUtils.toGregorianCalendar(cruEvent.endDate);

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, cruEvent.name);
        values.put(CalendarContract.Events.DESCRIPTION, cruEvent.description);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, cruEvent.startDate.getZone().toString());

        RxPermissions.getInstance(context)
            .request(Manifest.permission.WRITE_CALENDAR)
            .subscribe(granted -> {
                if (granted)
                {
                    try
                    {
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                        long eventID = Long.parseLong(uri.getLastPathSegment());

                        if (eventID > -1)
                            Observable.just(new Pair<>(cruEvent.id, eventID)).subscribe(parentSubscriber);
                    } catch (SecurityException e)
                    {
                        Logger.e(e, "Permission error");
                    }
                } else
                {
                    Logger.d("Permission denied");

                }
            });
    }

    public void removeEventFromCalendar(Context context, CruEvent cruEvent, long eventID, Observer<Pair<String, Long>> parentSubscriber)
    {
        ContentResolver cr = context.getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        cr.delete(deleteUri, null, null);
        Observable.just(new Pair<>(cruEvent.id, -1l)).subscribe(parentSubscriber);
    }
}
