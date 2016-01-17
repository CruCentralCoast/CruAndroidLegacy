package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class MinistrySubscription
{
    public Boolean mIsSubscribed;
    @SerializedName("image") public String mSubscriptionLogo;
    @SerializedName("slug") public String mSubscriptionSlug;

    public MinistrySubscription() {}

    public MinistrySubscription(String subscriptionLogo, String subscriptionSlug)
    {
        this.mIsSubscribed = false;
        this.mSubscriptionLogo = subscriptionLogo;
        this.mSubscriptionSlug = subscriptionSlug;
    }
}
