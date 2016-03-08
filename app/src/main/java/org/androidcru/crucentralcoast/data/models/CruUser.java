package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CruUser
{
    @SerializedName("name") public CruName name;
    @SerializedName("email") public String email;
    @SerializedName("phone") public String phoneNumber;

    public CruUser() {}

    public CruUser(CruName name, String email, String phoneNumber)
    {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
