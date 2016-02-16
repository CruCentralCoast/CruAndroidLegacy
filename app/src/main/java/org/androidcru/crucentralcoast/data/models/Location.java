package org.androidcru.crucentralcoast.data.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Location
{
    @SerializedName("postcode") String mPostcode;
    @SerializedName("state") String mState;
    @SerializedName("suburb") String mSuburb;
    @SerializedName("street1") String mStreet1;
    @SerializedName("country") String mCountry;
    public LatLng preciseLocation;

    /**
     * Required for Gson/RetroFit
     */
    public Location() {}

    public Location(String postcode, String state, String suburb, String street1, String country)
    {
        this.mPostcode = postcode;
        this.mState = state;
        this.mSuburb = suburb;
        this.mStreet1 = street1;
        this.mCountry = country;
    }

    public String getAsQuery()
    {
        String locString = String.format("%s %s, %s, %s, %s", mStreet1, mSuburb, mState, mPostcode, mCountry);
        return locString.replace(" ", "+");
    }


    public String toString()
    {
        return String.format("%s %s, %s, %s, %s", mStreet1, mSuburb, mState, mPostcode, mCountry);
    }
}
