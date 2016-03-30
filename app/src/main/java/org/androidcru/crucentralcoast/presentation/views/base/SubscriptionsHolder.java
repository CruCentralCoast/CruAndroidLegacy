package org.androidcru.crucentralcoast.presentation.views.base;

import rx.Subscription;

public interface SubscriptionsHolder
{
    void addSubscription(Subscription s);
    void clearSubscriptions();
}
