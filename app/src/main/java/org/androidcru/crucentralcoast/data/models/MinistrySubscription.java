package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

@Parcel
public class MinistrySubscription
{
    public static final String sCruImage = "image";
    public static final String sSubscriptionId = "_id";
    public static final String sCampusId = "campuses";

    @SerializedName(sCruImage) public CruImage cruImage;
    @SerializedName(sSubscriptionId) public String subscriptionId;
    @SerializedName(sCampusId) public ArrayList<String> campusId;

    @ParcelConstructor
    protected MinistrySubscription() {}
}
