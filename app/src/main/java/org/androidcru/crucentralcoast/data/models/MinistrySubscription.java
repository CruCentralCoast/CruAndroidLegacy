package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class MinistrySubscription
{
    public boolean mIsSubscribed;
    @SerializedName("image") public CruImage mCruImage;
    @SerializedName("slug") public String mSubscriptionSlug;

    public MinistrySubscription() {}

    public MinistrySubscription(CruImage cruImage, String subscriptionSlug)
    {
        this.mIsSubscribed = false;
        this.mCruImage = cruImage;
        this.mSubscriptionSlug = subscriptionSlug;
    }
}
