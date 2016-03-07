package org.androidcru.crucentralcoast.data.providers.util;

import com.orhanobut.logger.Logger;

import rx.Observable;

public class RxLoggingUtil
{
    public static <T> Observable.Transformer<T, T> log(String tag) {
        return o -> o
                .doOnNext(it -> Logger.t(tag, 0).d("onNext: %s", it))
                .doOnError(it -> Logger.t(tag).e("onError: %s", it))
                .doOnCompleted(() -> Logger.t(tag, 0).d("onCompleted"))
                .doOnSubscribe(() -> Logger.t(tag, 0).d("onSubscribe"))
                .doOnUnsubscribe(() -> Logger.t(tag, 0).d("onUnsubscribe"));
    }
}
