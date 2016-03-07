package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class PassengerProvider
{

    private static CruApiService mCruService = ApiProvider.getService();

    public static Observable<Passenger> addPassenger(Passenger passenger)
    {
        return mCruService.createPassenger(passenger)
                .compose(RxComposeUtil.network());
    }

    public static Observable<List<Passenger>> getPassengers(List<String> passengers)
    {
        return Observable.from(passengers)
                .compose(RxComposeUtil.network())
                .flatMap(passengerId -> {
                    return mCruService.findSinglePassenger(passengerId)
                            .flatMap(passengers1 -> {
                                return Observable.from(passengers1);
                            })
                            .subscribeOn(Schedulers.io());
                })
                .toList();
    }

}
