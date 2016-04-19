package org.androidcru.crucentralcoast.data.models;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

@Parcel
public final class Resource implements Dateable
{
    public static final String sId = "_id";
    public static final String sTitle = "title";
    public static final String sDate = "date";
    public static final String sUrl = "url";
    public static final String sResourceType = "type";
    public static final String sTagIds = "tags";

    @SerializedName(sId) public String id;
    @SerializedName(sTitle) public String title;
    @SerializedName(sUrl) public String url;
    @SerializedName(sResourceType) public ResourceType resourceType;
    @SerializedName(sDate) ZonedDateTime date;
    @SerializedName(sTagIds) public List<String> tagIds;

    public ArrayList<ResourceTag> tags;

    private static final String delimiter = ", ";

    @ParcelConstructor
    Resource() {}

    public String formatTags()
    {
        String result = "";

        for (ResourceTag str : tags)
            result += str.title + delimiter;

        if (!tags.isEmpty())
            result = result.substring(0, result.length() - delimiter.length());

        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return id.equals(resource.id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public ZonedDateTime getDate()
    {
        return date;
    }

    public enum ResourceType
    {
        ARTICLE("article"), AUDIO("audio"), VIDEO("video");

        private String type;

        ResourceType(String type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return type;
        }
    }
}
