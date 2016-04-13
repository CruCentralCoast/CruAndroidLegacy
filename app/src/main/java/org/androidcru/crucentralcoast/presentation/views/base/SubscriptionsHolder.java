package org.androidcru.crucentralcoast.presentation.views.base;

import android.content.Context;

import rx.Subscription;

public interface SubscriptionsHolder
{
    void addSubscription(Subscription s);
    void clearSubscriptions();
    Context getContext();
}
