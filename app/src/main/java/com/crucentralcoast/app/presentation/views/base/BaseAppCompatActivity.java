package com.crucentralcoast.app.presentation.views.base;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.crucentralcoast.app.R;

import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseAppCompatActivity extends AppCompatActivity implements SubscriptionsHolder
{
    private CompositeSubscription subscriptions = new CompositeSubscription();
    public Unbinder unbinder;
    private AlertDialog alertDialog;

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

    public void displayAutoFillDialog()
    {
        if(alertDialog == null)
        {
            alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Loading...")
                    .setView(R.layout.progress_layout1)
                    .create();
        }

        alertDialog.show();
    }

    public void hideAutoFillDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
