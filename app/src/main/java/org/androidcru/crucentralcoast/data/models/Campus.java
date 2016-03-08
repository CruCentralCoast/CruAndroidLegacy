package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Java representation of the model from the server
 */
@Parcel
public class Campus
{
    @SerializedName("_id") public String id;
    @SerializedName("name") public String campusName;

    /**
     * Required for Gson/RetroFit
     */
    public Campus() {}

    public Campus(String id, String campusName)
    {
        this.id = id;
        this.campusName = campusName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campus campus = (Campus) o;

        return id.equals(campus.id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }
}
