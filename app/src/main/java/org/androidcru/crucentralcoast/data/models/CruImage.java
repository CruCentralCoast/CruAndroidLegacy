package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class CruImage
{
    @SerializedName("url") public String mURL;
    @SerializedName("width") public int mWidth;
    @SerializedName("height") public int mHeight;

    /**
     * Required for Gson/RetroFit
     */
    public CruImage() {}

    public CruImage(String URL, int width, int height) {
        this.mURL = URL;
        this.mWidth = width;
        this.mHeight = height;
    }
}