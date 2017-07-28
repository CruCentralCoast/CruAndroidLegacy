package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class ResourceTag
{
    public static final String sId = "_id";
    public static final String sTitle = "title";

    public static final String SPECIAL_LEADER_ID = "-1";

    @SerializedName(sId) public String id;
    @SerializedName(sTitle) public String title;

    @ParcelConstructor
    public ResourceTag(String id, String title)
    {
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceTag that = (ResourceTag) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        return title;
    }
}
