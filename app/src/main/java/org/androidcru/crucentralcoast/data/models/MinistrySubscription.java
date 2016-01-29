package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MinistrySubscription
{
    public boolean mIsSubscribed;
    @SerializedName("image") public CruImage mCruImage;
    @SerializedName("slug") public String mSubscriptionSlug;
    @SerializedName("campuses") public ArrayList<String> mCampusId;

    /**
     * Required for Gson/RetroFit
     */
    public MinistrySubscription() {}

    public MinistrySubscription(CruImage cruImage, String subscriptionSlug, ArrayList<String> campusId)
    {
        this.mIsSubscribed = false;
        this.mCruImage = cruImage;
        this.mSubscriptionSlug = subscriptionSlug;
        this.mCampusId = campusId;
    }
}
