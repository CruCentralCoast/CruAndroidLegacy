package org.androidcru.crucentralcoast.presentation.providers;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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

    public void addEventToCalendar(Activity activity, CruEvent cruEvent, Observer<Pair<String, Long>> parentSubscriber)
    {
        Calendar beginTime = DateTimeUtils.toGregorianCalendar(cruEvent.mStartDate);
        Calendar endTime = DateTimeUtils.toGregorianCalendar(cruEvent.mEndDate);

        ContentResolver cr = activity.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, cruEvent.mName);
        values.put(CalendarContract.Events.DESCRIPTION, cruEvent.mDescription);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, cruEvent.mStartDate.getZone().toString());

        RxPermissions.getInstance(activity)
            .request(Manifest.permission.WRITE_CALENDAR)
            .subscribe(granted -> {
                if (granted)
                {
                    try
                    {
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                        long eventID = Long.parseLong(uri.getLastPathSegment());

                        if (eventID > -1)
                            Observable.just(new Pair<>(cruEvent.mId, eventID)).subscribe(parentSubscriber);
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

    public void removeEventFromCalendar(Activity activity, CruEvent cruEvent, long eventID, Observer<Pair<String, Long>> parentSubscriber)
    {
        ContentResolver cr = activity.getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        cr.delete(deleteUri, null, null);
        Observable.just(new Pair<>(cruEvent.mId, -1l)).subscribe(parentSubscriber);
    }
}
