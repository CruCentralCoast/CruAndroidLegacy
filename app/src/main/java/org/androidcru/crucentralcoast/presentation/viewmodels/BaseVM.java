package org.androidcru.crucentralcoast.presentation.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

public class BaseVM
{
    protected View rootView;
    public Context context;

    public BaseVM(View rootView)
    {
        this.rootView = rootView;
        this.context = rootView.getContext();
        ButterKnife.bind(this, rootView);
    }

    public BaseVM(Activity activity)
    {
        this.rootView = activity.findViewById(android.R.id.content);
        this.context = activity;
        ButterKnife.bind(this, activity);
    }

    public BaseVM(Fragment fragment)
    {
        this.rootView = fragment.getView();
        this.context = fragment.getContext();
        ButterKnife.bind(this, rootView);
    }

    public void rebind(Object target)
    {
        ButterKnife.bind(target, rootView);
    }

}
