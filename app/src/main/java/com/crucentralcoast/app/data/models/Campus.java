package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Java representation of the model from the server
 */
@Parcel()
public final class Campus
{
    public static final String sId = "_id";
    public static final String sName = "name";

    @SerializedName(sId) public String id;
    @SerializedName(sName) public String campusName;

    @ParcelConstructor
    Campus () {}

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
