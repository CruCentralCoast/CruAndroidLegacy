package org.androidcru.crucentralcoast.data.providers.observer;

import org.androidcru.crucentralcoast.CruApplication;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.functions.Action0;
import timber.log.Timber;

public class ObserverUtil
{
    /**
     * Creates an Observer which can also contains special callbacks for no elements from the stream and
     * network connectivity problems resulting from the stream.
     * @param <T> Type of the Observer
     * @param observer Original RxObserver for onNext, onError, and onCompleted
     * @param onEmpty Action0 to call in the event there are no elements from the stream
     * @param onNoNetwork Action0 to call in the event there is no network connectivity
     * @return CruObserver which handles onEmpty() and onNoNetwork() in addition to all normal Observer calls
     */
    public static <T> CruObserver<T> create(Observer<T> observer, Action0 onEmpty, Action0 onNoNetwork, Action0 onNetworkError)
    {
        return new CruObserver<T>()
        {
            boolean streamEmpty = true;

            @Override
            public void onEmpty()
            {
                onEmpty.call();
            }

            @Override
            public void onNoNetwork()
            {
                onNoNetwork.call();
            }

            @Override
            public void onCompleted()
            {
                if(streamEmpty)
                    onEmpty();
                //allows the observer to be reused
                streamEmpty = true;
                observer.onCompleted();
            }

            @Override
            public void onError(Throwable e)
            {
                if(e instanceof HttpException)
                {
                    Timber.e(e, "HTTP Error in Observer");
                    onNetworkError.call();
                }
                if(e instanceof IOException || e.getCause() instanceof IOException)
                {
                    if(!CruApplication.isOnline())
                        onNoNetwork();
                }
                //allows the observer to be reused
                streamEmpty = true;
                observer.onError(e);
            }

            @Override
            public void onNext(T t)
            {
                streamEmpty = false;
                observer.onNext(t);
            }
        };
    }


}
