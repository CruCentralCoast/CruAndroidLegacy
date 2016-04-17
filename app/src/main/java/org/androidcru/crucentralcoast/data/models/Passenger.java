package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Passenger
{
    public static final String sName = "name";
    public static final String sPhone = "phone";
    public static final String sGcmId = "gcmId";
    public static final String sDirection = "direction";
    public static final String sId = "_id";

    @SerializedName(sName) public String name;
    @SerializedName(sPhone) public String phone;
    @SerializedName(sGcmId) public String gcmId;
    @SerializedName(sDirection) public Ride.Direction direction;
    @SerializedName(sId) public String id;

    @ParcelConstructor
    public Passenger(String name, String phone,
                     String gcmId,
                     Ride.Direction direction)
    {
        this.name = name;
        this.phone = phone;
        this.gcmId = gcmId;
        this.direction = direction;
    }
}
