package org.androidcru.crucentralcoast.data.providers.observer;

import rx.Observer;

public interface CruObserver<T> extends Observer<T>
{
    public void onEmpty();

    public void onNoNetwork();
}
