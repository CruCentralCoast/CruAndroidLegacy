package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public final class Passenger {
    public static final String sName = "name";
    public static final String sPhone = "phone";
    public static final String sGcmId = "gcm_id";
    public static final String sDirection = "direction";
    public static final String sId = "_id";
    public static final String sEvent = "event";
    public static final String sRadius = "radius";
    public static final String sLocation = "location";
    public static final String sGenderPref = "gender_pref";

    @SerializedName(sName)
    public String name;
    @SerializedName(sPhone)
    public String phone;
    @SerializedName(sGcmId)
    public String fcmId;
    @SerializedName(sDirection)
    public Ride.Direction direction;
    @SerializedName(sId)
    public String id;
    @SerializedName(sEvent)
    public String eventId;
    @SerializedName(sLocation)
    public Location location;
    @SerializedName(sRadius)
    public double radius;
    @SerializedName(sGenderPref)
    public int genderPref;

    public double distance;

    @ParcelConstructor
    public Passenger(String name, String phone, String fcmId, Ride.Direction direction, String eventId) {
        this.name = name;
        this.phone = phone;
        this.fcmId = fcmId;
        this.direction = direction;
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Passenger
                && this.id.equals(((Passenger) obj).id);
    }
}
