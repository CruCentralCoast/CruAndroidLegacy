package com.crucentralcoast.app.presentation.views.base;

import android.content.Context;

import rx.Subscription;

public interface SubscriptionsHolder
{
    void addSubscription(Subscription s);
    void clearSubscriptions();
    Context getContext();
}
