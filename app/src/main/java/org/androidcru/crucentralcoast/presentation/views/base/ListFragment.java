package org.androidcru.crucentralcoast.presentation.views.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.androidcru.crucentralcoast.data.providers.observer.CruObserver;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Reusable class for Fragments with just a RecyclerView and emptyView for when that RecyclerView
 * is empty.
 *
 * Takes care of inflating a ViewStub when the time is right as well as a SwipeRefreshLayout workaround (see below)
 */
public class ListFragment extends BaseSupportFragment implements ListHelper
{
    protected ListHelperImpl helper;

    public ListFragment()
    {
        this.helper = new ListHelperImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        helper.onViewCreated(view);
    }

    @Override
    public View onEmpty(int layoutId)
    {
        return helper.onEmpty(layoutId);
    }

    @Override
    public void onNoNetwork()
    {
        helper.onNoNetwork();
    }

    @Override
    public void showContent()
    {
        helper.showContent();
    }

    @Override
    public <T> CruObserver<T> createListObserver(int emptyLayoutId, Action1<T> onNext)
    {
        return helper.createListObserver(emptyLayoutId, onNext);
    }

    @Override
    public <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty)
    {
        return helper.createListObserver(onNext, onEmpty);
    }

    @Override
    public <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty, Action0 onNoNetwork)
    {
        return helper.createListObserver(onNext, onEmpty, onNoNetwork);
    }

    @Override
    public void inflateEmptyView(View v, int layoutId)
    {
        helper.inflateEmptyView(v, layoutId);
    }
}
