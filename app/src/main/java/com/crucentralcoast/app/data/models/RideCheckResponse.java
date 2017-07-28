package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class RideCheckResponse
{
    public static final String sValue = "value";

    @SerializedName(sValue) public RideStatus value;

    @ParcelConstructor
    RideCheckResponse() {}

    public enum RideStatus {
        NEITHER, DRIVER, PASSENGER
    }
}
