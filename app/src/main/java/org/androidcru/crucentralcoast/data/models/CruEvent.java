package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import java.util.UUID;

public class CruEvent
{
    @SerializedName("name") public String mName;
    @SerializedName("description") public String mDescription;
    @SerializedName("startDate") public ZonedDateTime mStartDate;
    @SerializedName("endDate") public ZonedDateTime mEndDate;
    @SerializedName("rideSharingEnabled") public boolean mRideSharingEnabled;
    @SerializedName("location") public Location mLocation;
    @SerializedName("image") public CruImage mImage;
    @SerializedName("_id") public String mId;
    @SerializedName("url") public String mUrl;

    /**
     * Required by GSON/RetroFit in order to automatically create and populate via reflection
     */
    public CruEvent() {}

    public CruEvent(String name, String description, ZonedDateTime startDate, ZonedDateTime endDate, Location location, boolean rideSharingEnabled, String url, CruImage image)
    {
        this.mName = name;
        this.mDescription = description;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mLocation = location;
        this.mRideSharingEnabled = rideSharingEnabled;
        this.mImage = image;
        this.mUrl = url;
        this.mId = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public boolean isClean()
    {
        if(mName == null || mDescription == null || mStartDate == null || mEndDate == null
                || mLocation == null)
        {
            return false;
        }
        return true;
    }
}
