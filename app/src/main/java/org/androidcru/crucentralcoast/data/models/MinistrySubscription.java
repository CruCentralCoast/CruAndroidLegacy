package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

@Parcel
public final class MinistrySubscription
{
    public static final String sCruImage = "squareImageLink";
    public static final String sSubscriptionId = "_id";
    public static final String sCampusId = "campuses";

    @SerializedName(sCruImage) public String image;
    @SerializedName(sSubscriptionId) public String subscriptionId;
    @SerializedName(sCampusId) public ArrayList<String> campusId;

    @ParcelConstructor
    MinistrySubscription() {}
}
