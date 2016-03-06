package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

public class EventProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    public static Observable<ArrayList<CruEvent>> requestEvents()
    {

        return mCruService.getEvents()
                .retry()
                .subscribeOn(Schedulers.io());
    }

    public static Observable<CruEvent> requestCruEventByID(String id)
    {
        return mCruService.findSingleCruEvent(id)
                .retry()
                .subscribeOn(Schedulers.io())
                .flatMap(cruevents -> {
                    return Observable.from(cruevents);
                });
    }
}
