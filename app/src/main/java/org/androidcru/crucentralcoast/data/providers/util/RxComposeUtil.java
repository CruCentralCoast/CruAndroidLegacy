package org.androidcru.crucentralcoast.data.providers.util;

import org.androidcru.crucentralcoast.AppConstants;

import rx.Observable;
import rx.schedulers.Schedulers;

public class RxComposeUtil
{
    public static <T> Observable.Transformer<T, T> network() {
        return o -> o
                .subscribeOn(Schedulers.io())
                .retry(AppConstants.RETRY_ATTEMPTS)
                .onErrorResumeNext(Observable.empty());
    }
}
