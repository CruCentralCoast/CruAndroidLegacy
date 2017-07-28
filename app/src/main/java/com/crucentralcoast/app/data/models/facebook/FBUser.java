package com.crucentralcoast.app.data.models.facebook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FBUser
{
    @Expose @SerializedName("name") String mName;
    @Expose @SerializedName("id") String mId;
    @Expose @SerializedName("rsvp_status") String mRsvpStatus;
}
