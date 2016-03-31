package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author Connor Batch
 *
 * Deals with server calls to get the appropriate information in order to Manage Subscriptions.
 */
public final class SubscriptionProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    public static void requestMinistries(SubscriptionsHolder holder, Observer<ArrayList<MinistrySubscription>> observer)
    {
        Subscription s = requestMinistries()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    /**
     * Gets the list of available ministries from the server.
     * @return list of ministries
     */
    protected static Observable<ArrayList<MinistrySubscription>> requestMinistries()
    {
        return mCruService.getMinistries()
                .compose(RxLoggingUtil.log("MINISTRIES"))
                .compose(RxComposeUtil.network());
    }

    public static void requestCampuses(SubscriptionsHolder holder, Observer<ArrayList<Campus>> observer)
    {
        Subscription s = requestCampuses()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    /**
     * Gets the list of campuses from the server.
     */
    protected static Observable<ArrayList<Campus>> requestCampuses()
    {
        return mCruService.getCampuses()
                .compose(RxComposeUtil.network());
    }

    public static void requestCampusMinistryMap(SubscriptionsHolder holder, Observer<HashMap<Campus, ArrayList<MinistrySubscription>>> observer)
    {
        Subscription s = requestCampusMinistryMap()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<HashMap<Campus, ArrayList<MinistrySubscription>>> requestCampusMinistryMap()
    {
        return Observable.create(new Observable.OnSubscribe<HashMap<Campus, ArrayList<MinistrySubscription>>>()
        {
            @Override
            public void call(Subscriber<? super HashMap<Campus, ArrayList<MinistrySubscription>>> subscriber)
            {
                ArrayList<Campus> campuses = requestCampuses().toBlocking().single();
                ArrayList<MinistrySubscription> ministries = requestMinistries().toBlocking().single();

                HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap = new HashMap<>();

                for (MinistrySubscription m : ministries)
                {
                    Campus selectedCampus = null;

                    // set the selected campus for this ministry
                    for (Campus c : campuses)
                        if (m.campusId.get(0).equals(c.id))
                            selectedCampus = c;

                    if (campusMinistryMap.containsKey(selectedCampus))
                    {
                        ArrayList<MinistrySubscription> ministriesSoFar = campusMinistryMap.get(selectedCampus);
                        ministriesSoFar.add(m);
                        campusMinistryMap.put(selectedCampus, ministriesSoFar);
                    }
                    else
                    {
                        ArrayList<MinistrySubscription> newMinistries = new ArrayList<>();
                        newMinistries.add(m);
                        campusMinistryMap.put(selectedCampus, newMinistries);
                    }
                }

                subscriber.onNext(campusMinistryMap);
                subscriber.onCompleted();
            }
        })
        .compose(RxComposeUtil.network());
    }
}
