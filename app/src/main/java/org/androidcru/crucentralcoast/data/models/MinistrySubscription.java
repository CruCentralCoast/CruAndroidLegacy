package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class MinistrySubscription
{
    @SerializedName("isSubscribed") public Boolean mIsSubscribed;
    @SerializedName("subscriptionLogo") public String mSubscriptionLogo;
    @SerializedName("subscriptionSlug") public String mSubscriptionSlug;

    public MinistrySubscription(String subscriptionLogo, String subscriptionSlug)
    {
        this.mIsSubscribed = false;
        this.mSubscriptionLogo = subscriptionLogo;
        this.mSubscriptionSlug = subscriptionSlug;
    }
}
