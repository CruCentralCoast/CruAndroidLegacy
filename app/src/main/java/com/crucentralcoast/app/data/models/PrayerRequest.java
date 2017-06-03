package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by brittanyberlanga on 5/6/17.
 */

@Parcel
public class PrayerRequest {
    public static final String sId = "_id";
    public static final String sFcmId = "fcm_id";
    public static final String sCreatedAt = "createdAt";
    public static final String sLeadersOnly = "leadersOnly";
    public static final String sGenderPreference = "genderPreference";
    public static final String sContact = "contact";
    public static final String sContactLeader = "contactLeader";
    public static final String sContacted = "contacted";
    public static final String sContactInfo = "contactInfo";
    public static final String sPrayerResponse = "prayerResponse";
    public static final String sPrayerResponseCount = "prayerResponseCount";
    public static final String sPrayer = "prayer";

    @SerializedName(sId) public String id;
    @SerializedName(sFcmId) public String fcmId;
    @SerializedName(sCreatedAt) public Date creationDate;
    @SerializedName(sLeadersOnly) public boolean leadersOnly;
    @SerializedName(sGenderPreference) public Ride.Gender genderPreference;
    @SerializedName(sContact) public boolean contact;
    @SerializedName(sContactLeader) public String contactLeader;
    @SerializedName(sContacted) public boolean contacted;
    @SerializedName(sContactInfo) public String contactInfo;
    @SerializedName(sPrayerResponse) public List<PrayerResponse> prayerResponses;
    @SerializedName(sPrayerResponseCount) public int prayerResponseCount;
    @SerializedName(sPrayer) public String prayer;

    @ParcelConstructor
    public PrayerRequest(String fcmId, boolean leadersOnly, String prayer) {
        this.fcmId = fcmId;
        this.leadersOnly = leadersOnly;
        this.prayer = prayer;
    }
}
