package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Location
{
    public static final String sPostcode = "postcode";
    public static final String sState = "state";
    public static final String sSuburb = "suburb";
    public static final String sStreet1 = "street1";
    public static final String sCountry = "country";
    public static final String sGeo = "geo";

    @SerializedName(sPostcode) public String postcode;
    @SerializedName(sState) public String state;
    @SerializedName(sSuburb) public String suburb;
    @SerializedName(sStreet1) public String street1;
    @SerializedName(sCountry) public String country;
    @SerializedName(sGeo) public double[] geo;

    @ParcelConstructor
    public Location(String postcode, String state,
                    String suburb, String street1,
                    String country, double[] geo)
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
