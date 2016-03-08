package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CruName
{
    @SerializedName("last") public String lastName;
    @SerializedName("first") public String firstName;

    public CruName() {}

    public CruName(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
