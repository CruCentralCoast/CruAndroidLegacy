package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

@Parcel
public class CruEvent
{
    public static final String sName = "name";
    public static final String sDescription = "description";
    public static final String sStartDate = "startDate";
    public static final String sEndDate = "endDate";
    public static final String sRideSharingEnabled = "rideSharingEnabled";
    public static final String sLocation = "location";
    public static final String sImage = "image";
    public static final String sId = "_id";
    public static final String sUrl = "url";
    public static final String sParentMinistrySubscriptions = "parentMinistries";

    @SerializedName(sName) public String name;
    @SerializedName(sDescription) public String description;
    @SerializedName(sStartDate) public ZonedDateTime startDate;
    @SerializedName(sEndDate) public ZonedDateTime endDate;
    @SerializedName(sRideSharingEnabled) public boolean rideSharingEnabled;
    @SerializedName(sLocation) public Location location;
    @SerializedName(sImage) public CruImage image;
    @SerializedName(sId) public String id;
    @SerializedName(sUrl) public String url;
    @SerializedName(sParentMinistrySubscriptions) public ArrayList<String> parentMinistrySubscriptions;

    @ParcelConstructor
    protected CruEvent() {}

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
}
