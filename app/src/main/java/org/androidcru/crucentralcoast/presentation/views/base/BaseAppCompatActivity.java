package org.androidcru.crucentralcoast.presentation.views.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseAppCompatActivity extends AppCompatActivity implements SubscriptionsHolder
{
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
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
        return this;
    }
}
