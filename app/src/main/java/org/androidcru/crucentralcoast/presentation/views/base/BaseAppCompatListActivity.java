package org.androidcru.crucentralcoast.presentation.views.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import org.androidcru.crucentralcoast.data.providers.observer.CruObserver;

import rx.functions.Action0;
import rx.functions.Action1;

public class BaseAppCompatListActivity extends BaseAppCompatActivity implements ListHelper
{

    protected ListHelperImpl helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        helper = new ListHelperImpl();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        View v = findViewById(android.R.id.content);
        helper.onViewCreated(v);
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
    public <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty)
    {
        return helper.createListObserver(onNext, onEmpty);
    }

    @Override
    public <T> CruObserver<T> createListObserver(int emptyLayoutId, Action1<T> onNext)
    {
        return helper.createListObserver(emptyLayoutId, onNext);
    }

    @Override
    public void inflateEmptyView(View v, int layoutId)
    {
        helper.inflateEmptyView(v, layoutId);
    }
}
