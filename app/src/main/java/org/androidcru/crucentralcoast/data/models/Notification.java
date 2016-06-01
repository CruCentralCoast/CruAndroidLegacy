package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

@Parcel
public class Notification
{
    public static final String sContent = "content";
    public static final String sTimestamp = "timestamp";
    public static final String sPayload = "payload";

    @SerializedName(sContent) public String content;
    @SerializedName(sTimestamp) public ZonedDateTime timestamp;
    @SerializedName(sPayload) public CruUser payload;

    @ParcelConstructor
    public Notification(String content, ZonedDateTime timestamp)
    {
        this.content = content;
        this.timestamp = timestamp;
    }

    public Notification(String content, ZonedDateTime timestamp, CruUser payload)
    {
        this(content, timestamp);
        this.payload = payload;
    }
}
