package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class CruUser
{
    @SerializedName("name") public CruName name;
    @SerializedName("email") public String email;

    public CruUser() {}

    public CruUser(CruName name, String email)
    {
        this.name = name;
        this.email = email;
    }
}
