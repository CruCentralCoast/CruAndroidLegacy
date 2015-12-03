package org.androidcru.crucentralcoast.data.models;

import android.widget.ImageView;

import retrofit.http.Url;

public class MinistrySubscription
{
    public Boolean isSubscribed;
    public String subscriptionLogo;
    public String subscriptionSlug;

    public MinistrySubscription(String subscriptionLogo, String subscriptionSlug)
    {
        this.isSubscribed = false;
        this.subscriptionLogo = subscriptionLogo;
        this.subscriptionSlug = subscriptionSlug;
    }
}
