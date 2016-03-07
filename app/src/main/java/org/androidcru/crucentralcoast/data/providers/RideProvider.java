package org.androidcru.crucentralcoast.data.providers;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class RideProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    public static Observable<List<Ride>> requestRides()
    {
        return mCruService.getRides()
                .compose(RxComposeUtil.network())
                .flatMap(rides -> {
                    Logger.d("Rides found");
                    return Observable.from(rides);
                })
                .map(ride -> {
                    PassengerProvider.getPassengers(ride.passengerIds)
                            .compose(RxLoggingUtil.log("PASSENGERS"))
                            .map(passengers -> ride.passengers = passengers)
                            .toBlocking()
                            .subscribe();
                    return ride;
                })
                .map(ride -> {
                    EventProvider.requestCruEventByID(ride.eventId)
                            .compose(RxLoggingUtil.log("EVENTS"))
                            .map(theEvent -> ride.event = theEvent)
                            .toBlocking()
                            .subscribe();
                    ;
                    return ride;
                })
                .toList();
    }

    public static Observable<List<Ride>> searchRides(Query query)
    {
        return mCruService.searchRides(query)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Ride> createRide(Ride ride)
    {
        return mCruService.postRide(ride)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Ride> updateRide(Ride ride)
    {
        return mCruService.updateRide(ride)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Void> addPassengerToRide(String passengerId, String rideId)
    {
        return mCruService.addPassenger(rideId, passengerId)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Void> dropPassengerFromRide(String passengerId, String rideId)
    {
        return mCruService.dropPassenger(rideId, passengerId)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Void> dropRide(String rideId)
    {
        return mCruService.dropRide(rideId)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Ride> requestRideByID(String id)
    {
        return mCruService.findSingleRide(id)
                .compose(RxComposeUtil.network())
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
