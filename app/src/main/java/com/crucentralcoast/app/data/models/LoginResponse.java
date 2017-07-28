package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class LoginResponse
{
    public static final String sSuccess = "success";
    public static final String sLeaderAPIKey = "leaderAPIKey";
    public static final String sUserId = "userId";

    @SerializedName(sSuccess) public boolean success;
    @SerializedName(sLeaderAPIKey) public String leaderAPIKey;
    @SerializedName(sUserId) public String userId;

    @ParcelConstructor
    LoginResponse() {}
}
