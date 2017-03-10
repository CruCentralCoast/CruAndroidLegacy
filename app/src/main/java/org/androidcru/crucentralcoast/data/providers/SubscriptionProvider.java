package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * @author Connor Batch
 *
 * Deals with server calls to get the appropriate information in order to Manage Subscriptions.
 */
public final class SubscriptionProvider
{
    private static CruApiService mCruService = CruApiProvider.getService();

    public static void requestMinistries(SubscriptionsHolder holder, Observer<List<MinistrySubscription>> observer)
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
    protected static Observable<List<MinistrySubscription>> requestMinistries()
    {
        return mCruService.getMinistries()
                .compose(RxLoggingUtil.log("MINISTRIES"))
                .compose(RxComposeUtil.network());
    }

    public static void requestCampuses(SubscriptionsHolder holder, Observer<List<Campus>> observer)
    {
        Subscription s = requestCampuses()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    /**
     * Gets the list of campuses from the server.
     */
    protected static Observable<List<Campus>> requestCampuses()
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
        return Observable.fromCallable(() -> {
            List<Campus> campuses = requestCampuses().toBlocking().single();
            List<MinistrySubscription> ministries = requestMinistries().toBlocking().single();

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

            return campusMinistryMap;
        })
        .compose(RxComposeUtil.network());
    }
}
