package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class CruUser
{
    @SerializedName("name") public CruName mName;
    @SerializedName("email") public String mEmail;
    @SerializedName("phone") public String phoneNumber;

    public CruUser() {}

    public CruUser(CruName name, String email, String phoneNumber)
    {
        this.mName = name;
        this.mEmail = email;
        this.phoneNumber = phoneNumber;
    }
}
