package org.androidcru.crucentralcoast.data.providers.util;

import timber.log.Timber;

import rx.Observable;

public class RxLoggingUtil
{
    public static <T> Observable.Transformer<T, T> log(String tag) {
        return o -> o
                .doOnNext(it -> Timber.tag(tag).d("onNext: %s", it))
                .doOnError(it -> Timber.tag(tag).e("onError: %s", it))
                .doOnCompleted(() -> Timber.tag(tag).d("onCompleted"))
                .doOnSubscribe(() -> Timber.tag(tag).d("onSubscribe"))
                .doOnUnsubscribe(() -> Timber.tag(tag).d("onUnsubscribe"));
    }
}
