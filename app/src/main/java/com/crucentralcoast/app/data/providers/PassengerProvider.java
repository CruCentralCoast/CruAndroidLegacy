package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.queries.ConditionsBuilder;
import com.crucentralcoast.app.data.models.queries.Query;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class PassengerProvider {

    private static CruApiService mCruService = CruApiProvider.getService();

    public static void addPassenger(SubscriptionsHolder holder, Observer<Passenger> observer, Passenger passenger) {
        Subscription s = addPassenger(passenger)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Passenger> addPassenger(Passenger passenger) {
        return mCruService.createPassenger(passenger)
                .compose(RxComposeUtil.network());
    }

    public static void getPassengers(SubscriptionsHolder holder, Observer<List<Passenger>> observer, List<String> passengers) {
        Subscription s = getPassengers(passengers)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Passenger>> getPassengers(List<String> passengers) {
        return mCruService.searchPassengers(new Query.Builder()
                .setCondition(new ConditionsBuilder()
                        .setField(Passenger.sId)
                        .addRestriction(ConditionsBuilder.OPERATOR.IN, passengers.toArray(new String[passengers.size()]))
                        .build())
                .build())
                .compose(RxComposeUtil.network());
    }

}
