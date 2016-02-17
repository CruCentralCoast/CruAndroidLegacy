package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class Passenger
{
    @SerializedName("name") public String name;
    @SerializedName("phone") public String phone;
    @SerializedName("gcm_id") public String gcm_id;
    @SerializedName("direction") public Ride.Direction direction;
    @SerializedName("_id") public String id;

    public Passenger() {}

    public Passenger(String name, String phone, String gcm_id, Ride.Direction direction)
    {
        this.name = name;
        this.phone = phone;
        this.gcm_id = gcm_id;
        this.direction = direction;
    }
}
