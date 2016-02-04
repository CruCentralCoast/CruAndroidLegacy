package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class RideProvider
{
    private static CruApiService mCruService = ApiProvider.getInstance().getService();
    private static RideProvider mInstance;

    private RideProvider() {}

    public static RideProvider getInstance()
    {
        if(mInstance == null)
            mInstance = new RideProvider();
        return mInstance;
    }

    public Observable<ArrayList<Ride>> requestRides()
    {
        return mCruService.getRides()
                .subscribeOn(Schedulers.io());
    }
}
