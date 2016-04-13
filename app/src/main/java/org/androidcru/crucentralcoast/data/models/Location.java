package org.androidcru.crucentralcoast.data.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Location
{
    @SerializedName("postcode") String postcode;
    @SerializedName("state") String state;
    @SerializedName("suburb") String suburb;
    @SerializedName("street1") String street1;
    @SerializedName("country") String country;
    @SerializedName("geo") public double[] geo;

    /**
     * Required for Gson/RetroFit
     */
    public Location() {}

    public Location(String postcode, String state, String suburb, String street1, String country, double[] geo)
    {
        this.postcode = postcode;
        this.state = state;
        this.suburb = suburb;
        this.street1 = street1;
        this.country = country;
        this.geo = geo;
    }

    public String getAsQuery()
    {
        String locString = String.format("%s %s, %s, %s, %s", street1, suburb, state, postcode, country);
        return locString.replace(" ", "+");
    }


    public String toString()
    {
        return String.format("%s %s, %s, %s, %s", street1, suburb, state, postcode, country);
    }
}
