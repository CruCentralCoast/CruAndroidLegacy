package org.androidcru.crucentralcoast.data.providers.util;

import org.androidcru.crucentralcoast.AppConstants;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class RxComposeUtil
{
    public static <T> Observable.Transformer<T, T> network() {
        return o -> o
                .retry(AppConstants.RETRY_ATTEMPTS)
                .onErrorResumeNext(Observable.empty());
    }

    public static <T> Observable.Transformer<T, T> ui() {
        return o -> o
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
