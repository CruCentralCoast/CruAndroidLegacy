package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CruImage
{
    @SerializedName("url") public String url;
    @SerializedName("width") public int width;
    @SerializedName("height") public int height;

    /**
     * Required for Gson/RetroFit
     */
    public CruImage() {}

    public CruImage(String URL, int width, int height) {
        this.url = URL;
        this.width = width;
        this.height = height;
    }
}