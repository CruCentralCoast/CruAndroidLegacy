package org.androidcru.crucentralcoast.presentation.views.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseSupportFragment extends Fragment implements SubscriptionsHolder

{
    private CompositeSubscription subscriptions = new CompositeSubscription();
    public Unbinder unbinder;

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        clearSubscriptions();
        unbinder.unbind();
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
