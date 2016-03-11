package org.androidcru.crucentralcoast.common;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.plugins.RxJavaObservableExecutionHook;

/**
 * Provides the hooks for both RxJava and Espresso so that Espresso knows when to wait
 * until RxJava subscriptions have completed.
 */

public final class RxIdlingResource extends RxJavaObservableExecutionHook implements IdlingResource
{
    public static final String TAG = "RxIdlingResource";

    static LogLevel LOG_LEVEL = LogLevel.FULL;

    private final AtomicInteger subscriptions = new AtomicInteger(0);

    private static RxIdlingResource INSTANCE;

    private ResourceCallback resourceCallback;

    private RxIdlingResource() {
        //private
    }

    public static RxIdlingResource get() {
        if (INSTANCE == null) {
            INSTANCE = new RxIdlingResource();
            Espresso.registerIdlingResources(INSTANCE);
        }
        return INSTANCE;
    }

    public static void setLogLevel(LogLevel logLevel)
    {
        LOG_LEVEL = logLevel;
    }

    /* ======================== */
    /* IdlingResource Overrides */
    /* ======================== */

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public boolean isIdleNow() {
        int activeSubscriptionCount = subscriptions.get();
        boolean isIdle = activeSubscriptionCount == 0;

        if (LOG_LEVEL == LogLevel.FULL)
        {
            Logger.t(TAG).i("activeSubscriptionCount: " + activeSubscriptionCount);
            Logger.t(TAG).i("isIdleNow: " + isIdle);
        }

        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        /*if (LOG_LEVEL.atOrAbove(DEBUG)) {
            Log.d(TAG, "registerIdleTransitionCallback");
        }*/
        this.resourceCallback = resourceCallback;
    }

    /* ======================================= */
    /* RxJavaObservableExecutionHook Overrides */
    /* ======================================= */

    @Override
    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance,
                                                          final Observable.OnSubscribe<T> onSubscribe) {
        int activeSubscriptionCount = subscriptions.incrementAndGet();
        if (LOG_LEVEL == LogLevel.FULL) {
            Logger.t(TAG).i(onSubscribe + " - onSubscribeStart: " + activeSubscriptionCount);
        }

        onSubscribe.call(new Subscriber<T>() {
            @Override
            public void onCompleted() {
                onFinally(onSubscribe, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                onFinally(onSubscribe, "onError");
            }

            @Override
            public void onNext(T t) {
                //nothing
            }
        });

        return onSubscribe;
    }



    private <T> void onFinally(Observable.OnSubscribe<T> onSubscribe, final String finalizeCaller) {
        int activeSubscriptionCount = subscriptions.decrementAndGet();
        if (LOG_LEVEL == LogLevel.FULL) {
            Logger.t(TAG).i(onSubscribe + " - " + finalizeCaller + ": " + activeSubscriptionCount);
        }
        if (activeSubscriptionCount == 0) {
            Logger.t(TAG).i("onTransitionToIdle");
            resourceCallback.onTransitionToIdle();
        }
    }
}
