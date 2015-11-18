package org.androidcru.crucentralcoast.data.models;

import android.widget.ImageView;

import retrofit.http.Url;

public class MinistrySubscription
{
    public String subscriptionLogo;

    public MinistrySubscription(String subscriptionLogo)
    {
        this.subscriptionLogo = subscriptionLogo;
    }
}
