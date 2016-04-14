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

    //Auto-generated equals and hashcode
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CruImage cruImage = (CruImage) o;

        if (width != cruImage.width) return false;
        if (height != cruImage.height) return false;
        return !(url != null ? !url.equals(cruImage.url) : cruImage.url != null);

    }

    @Override
    public int hashCode()
    {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}