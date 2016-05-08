package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

@Parcel
public final class SummerMission
{
    public static final String sDescription = "description";
    public static final String sName = "name";
    public static final String sImage = "image";
    public static final String sLeaders = "leaders";
    public static final String sStartDate = "startDate";
    public static final String sEndDate = "endDate";
    public static final String sUrl = "url";
    public static final String sId = "_id";

    @SerializedName(sId) public String id;
    @SerializedName(sDescription) public String description;
    @SerializedName(sName) public String name;
    @SerializedName(sImage) public Image image;
    @SerializedName(sLeaders) public String leaders;
    @SerializedName(sStartDate) public ZonedDateTime startDate;
    @SerializedName(sEndDate) public ZonedDateTime endDate;
    @SerializedName(sUrl) public String url;

    @ParcelConstructor
    SummerMission() {}

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummerMission that = (SummerMission) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }
}
