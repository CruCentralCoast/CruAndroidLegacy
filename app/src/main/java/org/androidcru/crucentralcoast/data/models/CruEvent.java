package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.UUID;

public class CruEvent
{
    @SerializedName("name") public String name;
    @SerializedName("description") public String description;
    @SerializedName("startDate") public ZonedDateTime startDate;
    @SerializedName("endDate") public ZonedDateTime endDate;
    @SerializedName("rideSharingEnabled") public boolean rideSharingEnabled;
    @SerializedName("location") public Location location;
    @SerializedName("image") public CruImage image;
    @SerializedName("_id") public String id;
    @SerializedName("url") public String url;
    @SerializedName("parentMinistries") public ArrayList<String> parentMinistrySubscriptions;

    /**
     * Required by GSON/RetroFit
     */
    public CruEvent() {}

    public CruEvent(String name, String description, ZonedDateTime startDate, ZonedDateTime endDate,
                    Location location, boolean rideSharingEnabled, String url, CruImage image, ArrayList<String> parentMinistrySubscriptions)
    {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.rideSharingEnabled = rideSharingEnabled;
        this.image = image;
        this.url = url;
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.parentMinistrySubscriptions = parentMinistrySubscriptions;
    }

    public boolean isClean()
    {
        if(name == null || description == null || startDate == null || endDate == null
                || location == null)
        {
            return false;
        }
        return true;
    }
}
