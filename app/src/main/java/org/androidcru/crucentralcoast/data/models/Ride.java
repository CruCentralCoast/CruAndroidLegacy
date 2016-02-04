package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

public class Ride
{
    @SerializedName("driverName") public String mDriverName;
    @SerializedName("driverNumber") public String mDriverNumber;
    @SerializedName("gender") public String mGender;
    @SerializedName("event") public String mEventId;
    @SerializedName("time") public ZonedDateTime mTime;
    @SerializedName("location") public Location mLocation;
    @SerializedName("passengers") public ArrayList<String> mPassengers;

    public Ride(String mDriverName, String mDriverNumber, String mGender, String mEventId, ZonedDateTime mTime, Location mLocation, ArrayList<String> mPassengers)
    {
        this.mDriverName = mDriverName;
        this.mDriverNumber = mDriverNumber;
        this.mGender = mGender;
        this.mEventId = mEventId;
        this.mTime = mTime;
        this.mLocation = mLocation;
        this.mPassengers = mPassengers;
    }

    public Ride() {}
}
