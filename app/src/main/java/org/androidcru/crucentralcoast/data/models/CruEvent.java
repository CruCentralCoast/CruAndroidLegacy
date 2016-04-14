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

    public CruEvent(String name, Location location, ZonedDateTime endDate, ZonedDateTime startDate)
    {
        this.name = name;
        this.location = location;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    @ParcelConstructor
    public CruEvent(String name, String description, ZonedDateTime startDate, ZonedDateTime endDate,
                    boolean rideSharingEnabled, Location location, CruImage image, String id,
                    String url, ArrayList<String> parentMinistrySubscriptions)
    {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rideSharingEnabled = rideSharingEnabled;
        this.location = location;
        this.image = image;
        this.id = id;
        this.url = url;
        this.parentMinistrySubscriptions = parentMinistrySubscriptions;
    }
}
