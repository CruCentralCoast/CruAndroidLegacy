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
    @SerializedName("_id") public String mId;

    /**
     * Required by GSON/RetroFit in order to automatically create and populate via reflection
     */
    public CruEvent() {}

    public CruEvent(String name, String description, ZonedDateTime startDate, ZonedDateTime endDate, Location location, boolean rideSharingEnabled)
    {
        this.mName = name;
        this.mDescription = description;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mLocation = location;
        this.mRideSharingEnabled = rideSharingEnabled;
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
