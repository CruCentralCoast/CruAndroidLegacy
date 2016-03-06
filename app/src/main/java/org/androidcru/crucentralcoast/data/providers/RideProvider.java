package org.androidcru.crucentralcoast.data.providers;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class RideProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    public static Observable<List<Ride>> requestRides()
    {
        return mCruService.getRides()
                .retry()
                .subscribeOn(Schedulers.io())
                .flatMap(rides -> {
                    Logger.d("Rides found");
                    return Observable.from(rides);
                })
                .map(ride -> {
                    PassengerProvider.getPassengers(ride.passengerIds)
                            .map(passengers -> ride.passengers = passengers)
                            .toBlocking()
                            .subscribe();
                    return ride;
                })
                .map(ride -> {
                    EventProvider.requestCruEventByID(ride.eventId)
                            .map(theEvent -> ride.event = theEvent)
                            .toBlocking()
                            .subscribe();
                    ;
                    return ride;
                })
                .subscribeOn(Schedulers.io())
                .toList();
    }

    public static Observable<Ride> createRide(Ride ride)
    {
        return mCruService.postRide(ride)
                .retry()
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Ride> updateRide(Ride ride)
    {
        return mCruService.updateRide(ride)
                .retry()
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Void> addPassengerToRide(String passengerId, String rideId)
    {
        return mCruService.addPassenger(rideId, passengerId)
                .retry()
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Void> dropPassengerFromRide(String passengerId, String rideId)
    {
        return mCruService.dropPassenger(rideId, passengerId)
                .retry()
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Void> dropRide(String rideId)
    {
        return mCruService.dropRide(rideId)
                .retry()
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Ride> requestRideByID(String id)
    {
        return mCruService.findSingleRide(id)
                .retry()
                .subscribeOn(Schedulers.io())
                .flatMap(rides -> {
                    Logger.d("Rides found");
                    return Observable.from(rides);
                })
                .map(ride -> {
                    PassengerProvider.getPassengers(ride.passengerIds)
                            .subscribeOn(Schedulers.io())
                            .map(passengers -> ride.passengers = passengers)
                            .toBlocking()
                            .subscribe();
                    return ride;
                });

    }
}
