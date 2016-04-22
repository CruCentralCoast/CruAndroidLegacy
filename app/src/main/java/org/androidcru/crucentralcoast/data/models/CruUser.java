package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class CruUser
{
    public static final String sId = "_id";
    public static final String sName = "name";
    public static final String sEmail = "email";
    public static final String sPhone = "phone";

    @SerializedName(sId) public String id;
    @SerializedName(sName) public CruName name;
    @SerializedName(sEmail) public String email;
    @SerializedName(sPhone) public String phone;

    @ParcelConstructor
    public CruUser(CruName name, String email, String phone)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CruUser cruUser = (CruUser) o;

        return id.equals(cruUser.id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }
}
