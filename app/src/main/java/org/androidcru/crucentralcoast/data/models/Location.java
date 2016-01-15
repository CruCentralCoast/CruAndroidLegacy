package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class Location
{
    @SerializedName("mPostcode") String mPostcode;
    @SerializedName("mState") String mState;
    @SerializedName("mSuburb") String mSuburb;
    @SerializedName("mStreet1") String mStreet1;
    @SerializedName("mCountry") String mCountry;

    public Location(String postcode, String state, String suburb, String street1, String country)
    {
        this.mPostcode = postcode;
        this.mState = state;
        this.mSuburb = suburb;
        this.mStreet1 = street1;
        this.mCountry = country;
    }
}
