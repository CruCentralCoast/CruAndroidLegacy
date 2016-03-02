package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class PassengerProvider
{

    private static CruApiService mCruService = ApiProvider.getService();
    private static PassengerProvider mInstance;

    private PassengerProvider() {}

    public static PassengerProvider getInstance()
    {
        if(mInstance == null)
            mInstance = new PassengerProvider();
        return mInstance;
    }

    public Observable<Passenger> addPassenger(Passenger passenger)
    {
        return mCruService.createPassenger(passenger)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Passenger>> getPassengers(List<String> passengers)
    {
        return Observable.from(passengers)
                .flatMap(passengerId -> {
                    return mCruService.findSinglePassenger(passengerId)
                            .flatMap(passengers1 -> {
                                return Observable.from(passengers1);
                            })
                            .subscribeOn(Schedulers.io());
                })
                .toList()
                .subscribeOn(Schedulers.io());
    }

}
