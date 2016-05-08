package org.androidcru.crucentralcoast.data.models.youtube;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public class YouTubeResponse
{
    public static final String sItems = "items";
    public static final String sNextPageToken = "nextPageToken";

    @SerializedName(sNextPageToken) public String nextPageToken;
    @SerializedName(sItems) public List<Snippet> items;

    @ParcelConstructor
    YouTubeResponse() {}
}
