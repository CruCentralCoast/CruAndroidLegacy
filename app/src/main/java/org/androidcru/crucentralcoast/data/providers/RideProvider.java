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

    public Ride requestRideByID(String id)
    {
//        ArrayList<Ride> newRides = new ArrayList<Ride>();
////        rx.Observable.from(requestRides())
//        requestRides()
//                .map(ride -> new Ride(ride.mDriverName, ride.mDriverNumber, ride.mGender, ride.mEventId, ride.mTime, ride.mLocation, ride.mPassengers))
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(newRides::add);
//
//        for (Ride r : newRides)
//        {
//            if (r.mEventId.equals(id))
//                return r;
//        }

        return null;
    }
}
