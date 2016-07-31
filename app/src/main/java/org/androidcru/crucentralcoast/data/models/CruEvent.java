package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

@Parcel
public final class CruEvent implements Dateable
{
    public static final String sName = "name";
    public static final String sDescription = "description";
    public static final String sStartDate = "startDate";
    public static final String sEndDate = "endDate";
    public static final String sRideSharingEnabled = "rideSharing";
    public static final String sLocation = "location";
    public static final String sImage = "imageLink";
    public static final String sId = "_id";
    public static final String sUrl = "url";
    public static final String sParentMinistrySubscriptions = "ministries";

    @SerializedName(sName) public String name;
    @SerializedName(sDescription) public String description;
    @SerializedName(sStartDate) public ZonedDateTime startDate;
    @SerializedName(sEndDate) public ZonedDateTime endDate;
    @SerializedName(sRideSharingEnabled) public boolean rideSharingEnabled;
    @SerializedName(sLocation) public Location location;
    @SerializedName(sImage) public String image;
    @SerializedName(sId) public String id;
    @SerializedName(sUrl) public String url;
    @SerializedName(sParentMinistrySubscriptions) public ArrayList<String> parentMinistrySubscriptions;

    public RideCheckResponse.RideStatus rideStatus;

    @ParcelConstructor
    CruEvent() {}

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CruEvent cruEvent = (CruEvent) o;

        return id != null ? id.equals(cruEvent.id) : cruEvent.id == null;

    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public ZonedDateTime getDate()
    {
        return startDate;
    }
}
