package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class CruName
{
    public static final String sLastName = "last";
    public static final String sFirstName = "first";

    @SerializedName(sFirstName) public String firstName;
    @SerializedName(sLastName) public String lastName;

    @ParcelConstructor
    public CruName(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
