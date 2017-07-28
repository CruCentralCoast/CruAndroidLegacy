package com.crucentralcoast.app.data.providers.observer;

import rx.Observer;

public interface CruObserver<T> extends Observer<T>
{
    public void onEmpty();

    public void onNoNetwork();
}
