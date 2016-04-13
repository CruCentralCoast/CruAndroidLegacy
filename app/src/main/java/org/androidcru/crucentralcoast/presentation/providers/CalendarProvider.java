package org.androidcru.crucentralcoast.presentation.providers;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.util.Pair;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.threeten.bp.DateTimeUtils;

import java.util.Calendar;

import rx.Observable;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;

public final class CalendarProvider
{
    public static void addEventToCalendar(Context context, CruEvent cruEvent, Observer<Pair<String, Long>> parentSubscriber)
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
                        {
                            CruApplication.getSharedPreferences().edit().putLong(cruEvent.id, eventID).commit();
                            Observable.just(new Pair<>(cruEvent.id, eventID)).subscribe(parentSubscriber);
                        }
                    } catch (SecurityException e)
                    {
                        Timber.e(e, "Permission error");
                    }
                } else
                {
                    Timber.d("Permission denied");

                }
            });
    }

    public static void removeEventFromCalendar(Context context, CruEvent cruEvent, long eventID, Observer<Pair<String, Long>> parentSubscriber)
    {
        RxPermissions.getInstance(context)
                .request(Manifest.permission.WRITE_CALENDAR)
                .subscribe(granted -> {
                    if (granted && eventID > -1)
                    {
                        ContentResolver cr = context.getContentResolver();
                        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                        cr.delete(deleteUri, null, null);
                        CruApplication.getSharedPreferences().edit().remove(cruEvent.id).commit();
                        Observable.just(new Pair<>(cruEvent.id, -1l)).subscribe(parentSubscriber);
                    }
                });
    }

    public static void updateEvent(Context context, CruEvent cruEvent, long eventID, Observer<Pair<String, Long>> parentSubscriber)
    {
        if(eventID > -1 && !isEventUpdated(context, cruEvent, eventID))
        {
            Observer<Pair<String, Long>> observer  = Observers.create(p -> {
                addEventToCalendar(context, cruEvent, parentSubscriber);
            });

            removeEventFromCalendar(context, cruEvent, eventID, observer);
        }
    }

    private static boolean isEventUpdated(Context context, CruEvent refEvent, long eventID)
    {
        Uri calUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        String[] columns = new String[] {
                CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND
        };

        Cursor cursor = context.getContentResolver().query(calUri, columns, null, null, null);
        Timber.d(DatabaseUtils.dumpCursorToString(cursor));
        boolean status = cursor != null && cursor.getCount() != 0 && cursor.moveToFirst() && cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)).equals(refEvent.name) &&
                cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)).equals(refEvent.description) &&
                cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)) == refEvent.startDate.toInstant().toEpochMilli() &&
                cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND)) == refEvent.endDate.toInstant().toEpochMilli();

        if(cursor != null)
            cursor.close();

        return status;

    }

    public static boolean hasCalendarPermission(Context context)
    {
        return RxPermissions.getInstance(context).isGranted(Manifest.permission.WRITE_CALENDAR);
    }
}
