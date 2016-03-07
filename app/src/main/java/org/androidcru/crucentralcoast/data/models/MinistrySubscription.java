package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class MinistrySubscription
{
    @SerializedName("image") public CruImage cruImage;
    @SerializedName("_id") public String subscriptionId;
    @SerializedName("campuses") public ArrayList<String> campusId;

    /**
     * Required for Gson/RetroFit
     */
    public MinistrySubscription() {}

    public MinistrySubscription(CruImage cruImage, String subscriptionId, ArrayList<String> campusId)
    {
        this.cruImage = cruImage;
        this.subscriptionId = subscriptionId;
        this.campusId = campusId;
    }
}
