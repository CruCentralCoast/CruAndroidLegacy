package org.androidcru.crucentralcoast.data.providers;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.List;

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

    public Observable<List<Ride>> requestRides()
    {
        return mCruService.getRides()
                .subscribeOn(Schedulers.io())
                .flatMap(rides -> {
                    Logger.d("Rides found");
                    return Observable.from(rides);
                })
                .map(ride -> {
                    PassengerProvider.getInstance().getPassengers(ride.passengerIds)
                            .map(passengers -> ride.passengers = passengers)
                            .toBlocking()
                            .subscribe();
                    return ride;
                })
                .toList()
                .subscribeOn(Schedulers.io());
    }

    public Observable<Ride> createRide(Ride ride)
    {
        return mCruService.postRide(ride)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Ride> updateRide(Ride ride)
    {
        return mCruService.updateRide(ride)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Void> addPassengerToRide(String passengerId, String rideId)
    {
        return mCruService.addPassenger(rideId, passengerId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Void> dropPassengerFromRide(String passengerId, String rideId)
    {
        return mCruService.dropPassenger(rideId, passengerId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Void> dropRide(String rideId)
    {
        return mCruService.dropRide(rideId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Ride> requestRideByID(String id)
    {
        return mCruService.findSingleRide(id)
                .subscribeOn(Schedulers.io())
                .flatMap(rides -> {
                    Logger.d("Rides found");
                    return Observable.from(rides);
                })
                .map(ride -> {
                    PassengerProvider.getInstance().getPassengers(ride.passengerIds)
                            .map(passengers -> ride.passengers = passengers)
                            .toBlocking()
                            .subscribe();
                    return ride;
                });

    }
}
