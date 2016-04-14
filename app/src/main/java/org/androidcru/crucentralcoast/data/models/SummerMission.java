package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

@Parcel
public class SummerMission
{
    public static final String sDescription = "description";
    public static final String sName = "name";
    public static final String sImage = "image";
    public static final String sLeaders = "leaders";
    public static final String sStartDate = "startDate";
    public static final String sEndDate = "endDate";
    public static final String sUrl = "url";

    @SerializedName(sDescription) public String description;
    @SerializedName(sName) public String name;
    @SerializedName(sImage) public CruImage image;
    @SerializedName(sLeaders) public String leaders;
    @SerializedName(sStartDate) public ZonedDateTime startDate;
    @SerializedName(sEndDate) public ZonedDateTime endDate;
    @SerializedName(sUrl) public String url;

    @ParcelConstructor
    public SummerMission(String description, String name,
                         CruImage image,String leaders,
                         ZonedDateTime startDate, ZonedDateTime endDate,
                         String url)
    {
        this.description = description;
        this.name = name;
        this.image = image;
        this.leaders = leaders;
        this.startDate = startDate;
        this.endDate = endDate;
        this.url = url;
    }
}
