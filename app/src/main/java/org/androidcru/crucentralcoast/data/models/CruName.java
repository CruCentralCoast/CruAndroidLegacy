package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class CruName
{
    @SerializedName("last") public String mLastName;
    @SerializedName("first") public String mFirstName;

    public CruName() {}

    public CruName(String firstName, String lastName)
    {
        this.mFirstName = firstName;
        this.mLastName = lastName;
    }
}
