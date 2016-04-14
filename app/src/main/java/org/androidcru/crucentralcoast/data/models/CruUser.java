package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class CruUser
{
    public static final String sName = "name";
    public static final String sEmail = "email";
    public static final String sPhone = "phone";
    
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
}
