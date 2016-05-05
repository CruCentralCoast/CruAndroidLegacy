package org.androidcru.crucentralcoast.data.providers.util;

import org.androidcru.crucentralcoast.AppConstants;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxComposeUtil
{
    public static <T> Observable.Transformer<T, T> network() {
        return o -> o
                .subscribeOn(Schedulers.io())
                .retry(AppConstants.RETRY_ATTEMPTS)
                .onErrorResumeNext(Observable.empty());
    }

    public static <T> Observable.Transformer<T, T> ui() {
        return o -> o
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, List<T>> toListOrEmpty()
    {
        return o -> o
                .toList()
                .flatMap(list -> {
                    if(list.isEmpty())
                        return Observable.empty();
                    else
                        return Observable.just(list);
                });
    }
}
