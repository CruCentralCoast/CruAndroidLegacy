package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.queries.Query;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.LocationUtil;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.providers.util.RxLoggingUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;
import com.crucentralcoast.app.util.MathUtil;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.threeten.bp.Duration;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public final class RideProvider {
    private static CruApiService mCruService = CruApiProvider.getService();

    private static Observable.Transformer<Ride, Ride> attachEvent = rideObservable -> rideObservable.flatMap(ride ->
            EventProvider.requestCruEventByID(ride.eventId)
                    .flatMap(event -> {
                        ride.event = event;
                        return Observable.just(ride);
                    })
    );

    private static Observable.Transformer<Ride, Ride> attachDistance(double[] location) {
        return rideObservable -> rideObservable
                .map(ride -> {
                    ride.distance = MathUtil.convertMeterToMiles(LocationUtil.distance(location[1], location[0], ride.location.geo[1],
                            ride.location.geo[0]));
                    return ride;
                })
                .filter(ride1 -> ride1.distance <= ride1.radius);
    }

    private static Observable.Transformer<Ride, List<Ride>> sortByTime(ZonedDateTime dateTime) {
        return rideObservable -> rideObservable
                .toSortedList((Ride ride1, Ride ride2) -> {
                    //TODO remove if the server can ever handle the correct datetimes
                    ZonedDateTime adjusted = dateTime.withZoneSameLocal(ZoneOffset.UTC);
                    return Duration.between(ride1.time, adjusted).abs().compareTo(Duration.between(ride2.time, adjusted).abs());
                });
    }

    public static void requestRides(SubscriptionsHolder holder, Observer<List<Ride>> observer) {
        Subscription s = requestRides()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestMyRidesDriver(SubscriptionsHolder holder, Observer<List<Ride>> observer, String gcmId) {
        Subscription s = requestRides()
                .compose(RxLoggingUtil.log("RIDES"))
                .flatMap(Observable::from)
                .filter(ride -> ride.fcmId.equals(gcmId))
                .compose(RxComposeUtil.toListOrEmpty())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestMyRidesPassenger(SubscriptionsHolder holder, Observer<List<Ride>> observer, String gcmId) {
        Subscription s = requestRides()
                .compose(RxComposeUtil.ui())
                .flatMap(Observable::from)
                .filter(ride -> {
                    boolean status = false;
                    for (Passenger p : ride.passengers) {
                        if (p.fcmId != null && p.fcmId.equals(SharedPreferencesUtil.getFCMID())) {
                            status = true;
                        }
                    }
                    return status;
                })
                .compose(RxComposeUtil.toListOrEmpty())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static Observable<List<Ride>> requestRides() {
        return mCruService.getRides()
                .compose(RxComposeUtil.network())
                .flatMap(Observable::from)
                .flatMap(ride -> PassengerProvider.getPassengers(ride.passengerIds)
                        .flatMap(passengers -> {
                            ride.passengers = passengers;
                            if (ride.passengers == null)
                                ride.passengers = new ArrayList<>();
                            return Observable.just(ride);
                        })
                        .switchIfEmpty(Observable.just(ride)
                                .map(ride1 -> {
                                    if (ride1.passengers == null)
                                        ride1.passengers = new ArrayList<>();
                                    return ride1;
                                }))
                )
                .compose(attachEvent)
                .compose(RxComposeUtil.toListOrEmpty());
    }

    public static Observable<List<Ride>> requestAllRides() {
        return requestRides()
                .compose(RxComposeUtil.network())
                .flatMap(Observable::from)
                .filter(ride -> {
                    boolean status = false;
                    String fcmId = SharedPreferencesUtil.getFCMID();
                    for (Passenger pass : ride.passengers) {
                        if (pass.fcmId != null && pass.fcmId.equals(fcmId)) {
                            status = true;
                        }
                    }
                    if (ride.fcmId.equals(fcmId)) {
                        status = true;
                    }
                    return status;
                })
                .compose(RxComposeUtil.toListOrEmpty());
    }

    public static void searchRides(SubscriptionsHolder holder, Observer<List<Ride>> observer, Query query, double[] latlng, ZonedDateTime dateTime) {
        Subscription s = searchRides(query, latlng, dateTime)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Ride>> searchRides(Query query, double[] latlng, ZonedDateTime time) {
        return mCruService.searchRides(query)
                .compose(RxComposeUtil.network())
                .flatMap(Observable::from)
                .compose(attachEvent)
                .compose(attachDistance(latlng))
                //.compose(sortByDistance)
                .compose(sortByTime(time))
                .flatMap(finalRides -> finalRides.isEmpty() ? Observable.empty() : Observable.just(finalRides));
    }

    public static void createRide(Observer<Ride> observer, Ride ride) {
        createRide(ride)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    protected static Observable<Ride> createRide(Ride ride) {
        return mCruService.postRide(ride)
                .compose(RxComposeUtil.network())
                .compose(attachEvent);
    }


    public static void updateRide(Observer<Ride> observer, Ride ride) {
        updateRide(ride.id, ride)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    protected static Observable<Ride> updateRide(String rideId, Ride ride) {
        return mCruService.updateRide(rideId, ride)
                .compose(RxComposeUtil.network())
                .compose(attachEvent);
    }


    public static void addPassengerToRide(SubscriptionsHolder holder, Observer<Void> observer, String rideId, String passengerId) {
        Subscription s = addPassengerToRide(rideId, passengerId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static Observable<Void> addPassengerToRide(String rideId, String passengerId) {
        return mCruService.addPassenger(rideId, passengerId)
                .compose(RxComposeUtil.network());
    }

    public static Observable<Void> addPassengersToRide(String rideId, List<Passenger> passengers) {
        for (Passenger passenger : passengers) {
            addPassengerToRide(rideId, passenger.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            aVoid -> {},
                            Timber::e,
                            () -> Timber.i("Added passenger " + passenger.id)
                    );
        }
        return Observable.empty();
    }

    public static void dropPassengerFromRide(SubscriptionsHolder holder, Observer<Void> observer, String rideId, String passengerId) {
        Subscription s = dropPassengerFromRide(rideId, passengerId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Void> dropPassengerFromRide(String rideId, String passengerId) {
        return mCruService.dropPassenger(rideId, passengerId)
                .compose(RxComposeUtil.network());
    }


    public static void dropRide(SubscriptionsHolder holder, Observer<Void> observer, String rideId) {
        Subscription s = dropRide(rideId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Void> dropRide(String rideId) {
        return mCruService.dropRide(rideId)
                .compose(RxComposeUtil.network());
    }


    public static void requestRideByID(SubscriptionsHolder holder, Observer<Ride> observer, String id) {
        Subscription s = requestRideByID(id)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Ride> requestRideByID(String id) {
        return mCruService.findSingleRide(id)
                .compose(RxComposeUtil.network())
                .flatMap(rides -> {
                    Timber.d("Rides found");
                    return Observable.from(rides);
                })
                .take(1)
                .flatMap(ride -> PassengerProvider.getPassengers(ride.passengerIds)
                        .defaultIfEmpty(new ArrayList<>())
                        .map(passengers -> ride.passengers = passengers)
                        .flatMap(passengers1 -> Observable.just(ride))
                )
                .compose(attachEvent);

    }
}
