package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ResourceTag
{
    @SerializedName("_id") public String id;
    @SerializedName("title") public String title;

    public ResourceTag() {}

    public ResourceTag(String id, String title)
    {
        this.id = id;
        this.title = title;
    }
}
