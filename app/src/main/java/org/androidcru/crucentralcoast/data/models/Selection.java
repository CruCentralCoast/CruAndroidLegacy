package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Selection
{
    public static final String sValue = "value";

    @SerializedName(sValue) public String value;

    @ParcelConstructor
    Selection(String value)
    {
        this.value = value;
    }
}
