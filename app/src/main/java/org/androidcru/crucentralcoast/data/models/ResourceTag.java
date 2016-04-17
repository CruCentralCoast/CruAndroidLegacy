package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class ResourceTag
{
    public static final String sId = "_id";
    public static final String sTitle = "title";

    @SerializedName(sId) public String id;
    @SerializedName(sTitle) public String title;

    @ParcelConstructor
    protected ResourceTag() {}

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
}
