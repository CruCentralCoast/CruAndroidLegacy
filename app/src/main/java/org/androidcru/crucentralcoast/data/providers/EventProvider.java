package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class EventProvider
{
    private static CruApiService cruService = ApiProvider.getService();

    public static void requestEvents(SubscriptionsHolder holder, Observer<ArrayList<CruEvent>> observer)
    {
        Subscription s = requestEvents()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<ArrayList<CruEvent>> requestEvents()
    {

        return cruService.getEvents()
                .compose(RxComposeUtil.network());
    }

    public static void requestCruEventByID(SubscriptionsHolder holder, Observer<CruEvent> observer, String id)
    {
        Subscription s = requestCruEventByID(id)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<CruEvent> requestCruEventByID(String id)
    {
        return cruService.findSingleCruEvent(id)
                .compose(RxComposeUtil.network())
                .flatMap(cruevents -> {
                    return Observable.from(cruevents);
                });
    }
}
