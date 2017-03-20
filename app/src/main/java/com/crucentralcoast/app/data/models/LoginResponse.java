package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class LoginResponse
{
    public static final String sSuccess = "success";
    public static final String sLeaderAPIKey = "LeaderAPIKey";

    @SerializedName(sSuccess) public boolean success;
    @SerializedName(sLeaderAPIKey) public String leaderAPIKey;

    @ParcelConstructor
    LoginResponse() {}
}
