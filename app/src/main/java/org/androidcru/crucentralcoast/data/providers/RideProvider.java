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

    public Observable<Ride> createRide(Ride ride)
    {
        return mCruService.postRide(ride)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Void> addPassengerToRide(String passengerId, String rideId)
    {
        return mCruService.addPassenger(rideId, passengerId)
                .subscribeOn(Schedulers.io());
    }

    public Ride requestRideByID(String id)
    {
//        ArrayList<Ride> newRides = new ArrayList<Ride>();
////        rx.Observable.from(requestRides())
//        requestRides()
//                .map(ride -> new Ride(ride.driverName, ride.driverNumber, ride.gender, ride.eventId, ride.time, ride.location, ride.passengers))
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(newRides::add);
//
//        for (Ride r : newRides)
//        {
//            if (r.eventId.equals(id))
//                return r;
//        }

        return null;
    }
}
