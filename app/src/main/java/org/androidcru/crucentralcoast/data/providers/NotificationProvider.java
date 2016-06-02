package org.androidcru.crucentralcoast.data.providers;

import android.os.AsyncTask;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;

import org.androidcru.crucentralcoast.data.models.Notification;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NotificationProvider
{
    private static final String sNotifications = "notifications";
    private static Type collectionType = new TypeToken<List<Notification>>() {}.getType();


    public static void getNotifications(SubscriptionsHolder holder, Observer<List<Notification>> observer)
    {
        holder.addSubscription(getNotifications()
                .compose(RxComposeUtil.ui())
                .subscribe(observer));
    }

    static Observable<List<Notification>> getNotifications()
    {
        try
        {
            if (!Reservoir.contains(sNotifications))
                return Observable.empty();
            return Reservoir
                    .getAsync(sNotifications, Notification.class, collectionType)
                    .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                    .onBackpressureDrop()
                    .toSortedList((Notification n1, Notification n2) -> {
                        return n2.timestamp.compareTo(n1.timestamp);
                    });
        }
        catch (Exception e)
        {
            Timber.e(e, "Reservoir error!");
            return Observable.error(e);
        }
    }

    public static void putNotification(Observer<Boolean> observer, Notification notification)
    {
        putNotification(notification)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    static Observable<Boolean> putNotification(Notification notification)
    {
        try
        {
            if (Reservoir.contains(sNotifications))
            {
                return Reservoir.getAsync(sNotifications, Notification.class, collectionType)
                    .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                    .onBackpressureDrop()
                    .toList()
                    .flatMap(notifications -> {
                        notifications.add(notification);
                        return Reservoir.putAsync(sNotifications, notifications)
                                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                                .onBackpressureDrop();
                    });
            }
            else
            {
                List<Notification> notifications = new ArrayList<>();
                notifications.add(notification);
                return Reservoir.putAsync(sNotifications, notifications)
                        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                        .onBackpressureDrop();
            }
        }
        catch(Exception e)
        {
            Timber.e(e, "Reservoir Error!");
            return Observable.empty();
        }
    }
}
