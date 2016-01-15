package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

public class CruEvent
{
    @SerializedName("mName") public String mName;
    @SerializedName("mDescription") public String mDescription;
    @SerializedName("mStartDate") public ZonedDateTime mStartDate;
    @SerializedName("mEndDate") public ZonedDateTime mEndDate;
    @SerializedName("mRideSharingEnabled") public boolean mRideSharingEnabled;
    @SerializedName("mLocation") public Location mLocation;

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
    }
}
