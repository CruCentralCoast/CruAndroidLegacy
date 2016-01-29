package org.androidcru.crucentralcoast.presentation.modelviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FBUserVM
{
    @Expose @SerializedName("name") String mName;
    @Expose @SerializedName("id") String mId;
    @Expose @SerializedName("rsvp_status") String mRsvpStatus;
}
