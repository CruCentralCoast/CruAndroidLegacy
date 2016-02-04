package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class CruUser
{
    @SerializedName("name") public CruName mName;
    @SerializedName("email") public String mEmail;

    public CruUser() {}

    public CruUser(CruName name, String email)
    {
        this.mName = name;
        this.mEmail = email;
    }
}
