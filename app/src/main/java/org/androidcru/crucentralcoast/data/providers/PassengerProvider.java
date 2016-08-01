package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class PassengerProvider
{

    private static CruApiService mCruService = CruApiProvider.getService();

    public static void addPassenger(SubscriptionsHolder holder, Observer<Passenger> observer, Passenger passenger)
    {
        Subscription s = addPassenger(passenger)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<Passenger> addPassenger(Passenger passenger)
    {
        return mCruService.createPassenger(passenger)
                .compose(RxComposeUtil.network());
    }

    public static void getPassengers(SubscriptionsHolder holder, Observer<List<Passenger>> observer, List<String> passengers)
    {
        Subscription s = getPassengers(passengers)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Passenger>> getPassengers(List<String> passengers)
    {
        return mCruService.searchPassengers(new Query.Builder()
                                                    .setCondition(new ConditionsBuilder()
                                                        .setField(Passenger.sId)
                                                        .addRestriction(ConditionsBuilder.OPERATOR.IN, passengers.toArray(new String[passengers.size()]))
                                                        .build())
                                                    .build())
                .compose(RxComposeUtil.network());
    }

}
