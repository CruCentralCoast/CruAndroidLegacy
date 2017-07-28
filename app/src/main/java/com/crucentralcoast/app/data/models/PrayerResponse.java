package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

/**
 * Created by brittanyberlanga on 5/22/17.
 */

@Parcel
public class PrayerResponse {
    public static final String sId = "_id";
    public static final String sFcmId = "fcm_id";
    public static final String sCreatedAt = "createdAt";
    public static final String sResponse = "response";
    public static final String sPrayerRequestId = "prayerRequestId";

    @SerializedName(sId) public String id;
    @SerializedName(sFcmId) public String fcmId;
    @SerializedName(sCreatedAt) public Date creationDate;
    @SerializedName(sResponse) public String response;
    @SerializedName(sPrayerRequestId) public String prayerRequestId;

    @ParcelConstructor
    public PrayerResponse(String fcmId, String response, String prayerRequestId) {
        this.fcmId = fcmId;
        this.response = response;
        this.prayerRequestId = prayerRequestId;
    }
}
