package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

@Parcel
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
        this.parentMinistrySubscriptions = parentMinistrySubscriptions;
    }

    //Auto-generated equals and hashcode
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CruEvent cruEvent = (CruEvent) o;

        if (rideSharingEnabled != cruEvent.rideSharingEnabled) return false;
        if (!name.equals(cruEvent.name)) return false;
        if (description != null ? !description.equals(cruEvent.description) : cruEvent.description != null)
            return false;
        if (!startDate.equals(cruEvent.startDate)) return false;
        if (!endDate.equals(cruEvent.endDate)) return false;
        if (!location.equals(cruEvent.location)) return false;
        if (image != null ? !image.equals(cruEvent.image) : cruEvent.image != null) return false;
        if (id != null ? !id.equals(cruEvent.id) : cruEvent.id != null) return false;
        return !(url != null ? !url.equals(cruEvent.url) : cruEvent.url != null) && !(parentMinistrySubscriptions != null ? !parentMinistrySubscriptions.equals(cruEvent.parentMinistrySubscriptions) : cruEvent.parentMinistrySubscriptions != null);

    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + (rideSharingEnabled ? 1 : 0);
        result = 31 * result + location.hashCode();
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (parentMinistrySubscriptions != null ? parentMinistrySubscriptions.hashCode() : 0);
        return result;
    }
}
