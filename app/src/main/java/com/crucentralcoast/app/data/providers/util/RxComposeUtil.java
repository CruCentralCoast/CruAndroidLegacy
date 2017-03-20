package com.crucentralcoast.app.data.providers.util;

import com.crucentralcoast.app.data.providers.api.CruApiProvider;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class RxComposeUtil
{
    public static <T> Observable.Transformer<T, T> network() {
        return o -> o
                .subscribeOn(CruApiProvider.getNetworkScheduler());
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
