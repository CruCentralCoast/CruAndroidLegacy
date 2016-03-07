package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public final class SubscriptionProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    /**
     * Gets the list of available ministries from the server.
     * @return list of ministries
     */
    public static Observable<ArrayList<MinistrySubscription>> requestMinistries()
    {
        return mCruService.getMinistries().subscribeOn(Schedulers.io());
    }

    /**
     * Gets the list of campuses from the server.
     */
    public static Observable<ArrayList<Campus>> requestCampuses()
    {
        return mCruService.getCampuses()
                .compose(RxComposeUtil.network());
    }

    public static Observable<HashMap<Campus, ArrayList<MinistrySubscription>>> requestCampusMinistryMap()
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

                    for (Campus c : campuses)
                        if (m.campusId.get(0).equals(c.id))
                            selectedCampus = c;

                    if (campusMinistryMap.containsKey(selectedCampus))
                    {
                        ArrayList<MinistrySubscription> ministriesSoFar = campusMinistryMap.get(selectedCampus);
                        ministriesSoFar.add(m);
                        campusMinistryMap.put(selectedCampus, ministriesSoFar);
                    } else
                    {
                        ArrayList<MinistrySubscription> newMinistries = new ArrayList<>();
                        newMinistries.add(m);
                        campusMinistryMap.put(selectedCampus, newMinistries);
                    }
                }

                subscriber.onNext(campusMinistryMap);
            }
        })
                .compose(RxComposeUtil.network());
    }
}
