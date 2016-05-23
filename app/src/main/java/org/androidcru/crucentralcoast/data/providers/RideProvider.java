package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.LocationUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.androidcru.crucentralcoast.util.MathUtil;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

public final class RideProvider
{
    private static CruApiService mCruService = CruApiProvider.getService();

    private static Observable.Transformer<Ride, Ride> attachEvent = rideObservable -> rideObservable.flatMap(ride -> {
        return EventProvider.requestCruEventByID(ride.eventId)
                .flatMap(event -> {
                    ride.event = event;
                    return Observable.just(ride);
                });
    });

    private static Observable.Transformer<Ride, Ride> attachDistance(double[] location)
    {
        return rideObservable -> rideObservable
                .map(ride -> {
                    ride.distance = MathUtil.convertMeterToMiles(LocationUtil.distance(location[0], location[1], ride.location.geo[1],
                            ride.location.geo[0]));
                    return ride;
                })
                .filter(ride1 -> {
                    return ride1.distance <= ride1.radius;
                });
    }

    private static Observable.Transformer<Ride, List<Ride>> sortByDistance = rideObservable -> rideObservable
            .toSortedList((Ride r1, Ride r2) -> {
                return Double.compare(r2.distance, r1.distance);
            });

    public static void requestRides(SubscriptionsHolder holder, Observer<List<Ride>> observer)
    {
        Subscription s = requestRides()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestMyRidesDriver(SubscriptionsHolder holder, Observer<List<Ride>> observer, String gcmId)
    {
        Subscription s = requestRides()
                .compose(RxComposeUtil.ui())
                .flatMap(rides -> Observable.from(rides))
                .filter(ride -> {
                    return ride.gcmID.equals(gcmId);
                })
                .compose(RxComposeUtil.toListOrEmpty())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestMyRidesPassenger(SubscriptionsHolder holder, Observer<List<Ride>> observer, String gcmId)
    {
        Subscription s = requestRides()
                .compose(RxComposeUtil.ui())
                .flatMap(rides -> Observable.from(rides))
                .filter(ride -> {
                    boolean status = false;
                    for (Passenger p : ride.passengers)
                    {
                        if (p.gcmId != null && p.gcmId.equals(SharedPreferencesUtil.getGCMID()))
                        {
                            status = true;
                        }
                    }
                    return status;
                })
                .compose(RxComposeUtil.toListOrEmpty())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Ride>> requestRides()
    {
        return mCruService.getRides()
                .flatMap(rides -> {
                    Timber.d("Rides found");
                    return Observable.from(rides);
                })
                .map(ride -> {
                    PassengerProvider.getPassengers(ride.passengerIds)
                            .compose(RxLoggingUtil.log("PASSENGERS"))
                            .map(passengers -> ride.passengers = passengers)
                            .toBlocking()
                            .subscribe();
                    if(ride.passengers == null)
                        ride.passengers = new ArrayList<Passenger>();
                    return ride;
                })
                .compose(attachEvent)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.toListOrEmpty());
    }



    public static void searchRides(SubscriptionsHolder holder, Observer<List<Ride>> observer, Query query, double[] latlng)
    {
        Subscription s = searchRides(query, latlng)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Ride>> searchRides(Query query, double[] latlng)
    {
        return mCruService.searchRides(query)
                .flatMap(rides -> {
                    return Observable.from(rides);
                })
                .compose(attachEvent)
                .compose(attachDistance(latlng))
                .compose(sortByDistance)
                .flatMap(finalRides -> {
                    return finalRides.isEmpty() ? Observable.empty() : Observable.just(finalRides);
                })
                .compose(RxComposeUtil.network());
    }



    public static void createRide(Observer<Ride> observer, Ride ride)
    {
        createRide(ride)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    protected static Observable<Ride> createRide(Ride ride)
    {
        return mCruService.postRide(ride)
                .compose(attachEvent)
                .compose(RxComposeUtil.network());
    }



    public static void updateRide(Observer<Ride> observer, Ride ride)
    {
        updateRide(ride.id, ride)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    protected static Observable<Ride> updateRide(String rideId, Ride ride)
    {
        return mCruService.updateRide(rideId, ride)
                .compose(attachEvent)
                .compose(RxComposeUtil.network());
    }



    public static void addPassengerToRide(SubscriptionsHolder holder, Observer<Void> observer, String rideId, String passengerId)
    {
        Subscription s = addPassengerToRide(rideId, passengerId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Void> addPassengerToRide(String rideId, String passengerId)
    {
        return mCruService.addPassenger(rideId, passengerId)
                .compose(RxComposeUtil.network());
    }



    public static void dropPassengerFromRide(SubscriptionsHolder holder, Observer<Void> observer, String rideId, String passengerId)
    {
        Subscription s = dropPassengerFromRide(rideId, passengerId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Void> dropPassengerFromRide(String rideId, String passengerId)
    {
        return mCruService.dropPassenger(rideId, passengerId)
                .compose(RxComposeUtil.network());
    }


    public static void dropRide(SubscriptionsHolder holder, Observer<Void> observer, String rideId)
    {
        Subscription s = dropRide(rideId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Void> dropRide(String rideId)
    {
        return mCruService.dropRide(rideId)
                .compose(RxComposeUtil.network());
    }


    public static void requestRideByID(SubscriptionsHolder holder, Observer<Ride> observer, String id)
    {
        Subscription s = requestRideByID(id)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Ride> requestRideByID(String id)
    {
        return mCruService.findSingleRide(id)
                .flatMap(rides -> {
                    Timber.d("Rides found");
                    return Observable.from(rides);
                })
                .take(1)
                .flatMap(ride -> {
                    return PassengerProvider.getPassengers(ride.passengerIds)
                            .defaultIfEmpty(new ArrayList<>())
                            .map(passengers -> ride.passengers = passengers)
                            .flatMap(passengers1 -> Observable.just(ride));
                })
                .compose(attachEvent)
                .compose(RxComposeUtil.network());

    }
}
