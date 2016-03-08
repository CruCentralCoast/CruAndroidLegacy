package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.ZonedDateTime;

@Parcel
public class SummerMission
{
    @SerializedName("description") public String description;
    @SerializedName("name") public String name;
    @SerializedName("image") public CruImage image;
    @SerializedName("leaders") public String leaders;
    @SerializedName("startDate") public ZonedDateTime startDate;
    @SerializedName("endDate") public ZonedDateTime endDate;
    @SerializedName("url") public String url;

    public SummerMission() {}

    public SummerMission(String description, String name, CruImage image, String leaders, ZonedDateTime startDate, ZonedDateTime endDate, String url)
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
