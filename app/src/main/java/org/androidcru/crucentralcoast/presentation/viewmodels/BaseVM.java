package org.androidcru.crucentralcoast.presentation.viewmodels;

import android.content.Context;
import android.view.View;

import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import butterknife.ButterKnife;

public class BaseVM
{
    protected View rootView;
    protected SubscriptionsHolder holder;
    public Context context;

    public BaseVM(BaseAppCompatActivity activity)
    {
        this.rootView = activity.findViewById(android.R.id.content);
        this.holder = activity;
        this.context = activity;
        ButterKnife.bind(this, activity);
    }

    public BaseVM(BaseSupportFragment fragment)
    {
        this.rootView = fragment.getView();
        this.context = fragment.getContext();
        this.holder = fragment;
        ButterKnife.bind(this, rootView);
    }

}
