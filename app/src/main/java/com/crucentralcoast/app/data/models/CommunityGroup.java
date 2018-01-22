package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;
import com.mobsandgeeks.saripaar.annotation.AssertFalse;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

@Parcel
public class CommunityGroup
{
    public static final String sId = "_id";
    public static final String sMinistry = "ministry";
    public static final String sName = "name";
    public static final String sDescription = "description";
    public static final String sMeetingTime = "meetingTime";
    public static final String sDayOfWeek = "dayOfWeek";
    public static final String sLeaders = "leaders";
    public static final String sType = "type";

    @SerializedName(sId) public String id;
    @SerializedName(sMinistry) public String ministry;
    @SerializedName(sName) public String name;
    @SerializedName(sDescription) public String description;
    @SerializedName(sMeetingTime) public ZonedDateTime meetingTime;
    @SerializedName(sDayOfWeek) public DayOfWeek dayOfWeek;
    @SerializedName(sLeaders) public ArrayList<CruUser> leaders;
    @SerializedName(sType) public String type;

    @ParcelConstructor
    CommunityGroup() {}

    public CommunityGroup(String cgID, String cgMinistry, String cgName,
                          String cgDescription, ZonedDateTime cgMeetingTime,
                          DayOfWeek cgDayOfWeek, String cgType, ArrayList<CruUser> cgLeaders) {
        this.id = cgID;
        this.ministry = cgMinistry;
        this.name = cgName;
        this.description = cgDescription;
        this.meetingTime = cgMeetingTime;
        this.dayOfWeek = cgDayOfWeek;
        this.type = cgType;
        this.leaders = cgLeaders;
    }
}
