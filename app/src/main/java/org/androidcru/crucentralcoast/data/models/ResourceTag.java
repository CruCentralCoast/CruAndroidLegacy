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
    public ResourceTag(String id, String title)
    {
        this.id = id;
        this.title = title;
    }
}
