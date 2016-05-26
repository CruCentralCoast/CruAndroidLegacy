package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

@Parcel
public class CommunityGroup
{
    public static final String sId = "_id";
    public static final String sMinistry = "ministry";
    public static final String sName = "name";
    public static final String sDescription = "description";
    public static final String sMeetingTime = "meetingTime";
    public static final String sLeaders = "leaders";

    @SerializedName(sId) public String id;
    @SerializedName(sMinistry) public String ministry;
    @SerializedName(sName) public String name;
    @SerializedName(sDescription) public String description;
    @SerializedName(sMeetingTime) public String meetingTime;
    @SerializedName(sLeaders) public ArrayList<String> leaders;

    @ParcelConstructor
    CommunityGroup() {}
}
