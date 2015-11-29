package org.androidcru.crucentralcoast.data.models;

public class Location
{
    String postcode;
    String state;
    String suburb;
    String street1;
    String country;

    public Location(String postcode, String state, String suburb, String street1, String country)
    {
        this.postcode = postcode;
        this.state = state;
        this.suburb = suburb;
        this.street1 = street1;
        this.country = country;
    }
}
