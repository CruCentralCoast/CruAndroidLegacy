package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class CruImage
{
    public static final String sUrl = "url";
    public static final String sWidth = "width";
    public static final String sHeight = "height";

    @SerializedName(sUrl) public String url;
    @SerializedName(sWidth) public int width;
    @SerializedName(sHeight) public int height;

    @ParcelConstructor
    CruImage() {}
}