package org.androidcru.crucentralcoast.presentation.views.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseSupportFragment extends Fragment implements SubscriptionsHolder

{
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        clearSubscriptions();
    }

    @Override
    public void addSubscription(Subscription s)
    {
        subscriptions.add(s);
    }

    @Override
    public void clearSubscriptions()
    {
        subscriptions.clear();
    }

    @Override
    public Context getContext()
    {
        return super.getContext();
    }
}
