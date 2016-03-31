package org.androidcru.crucentralcoast.data.models;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Resource
{
    @SerializedName("_id") public String id;
    @SerializedName("title") public String title;
    @SerializedName("url") public String url;
    @SerializedName("type") public ResourceType resourceType;
    @SerializedName("tags") public List<String> tagIds;
    public ArrayList<ResourceTag> tags;

    private final String delimiter = ", ";

    /**
     * Required for Gson/RetroFit
     */
    public Resource() {}

    public Resource(String title, String url, ResourceType resourceType, ArrayList<ResourceTag> tags)
    {
        this.title = title;
        this.url = url;
        this.resourceType = resourceType;
        this.tags = tags;
    }

    public String formatTags()
    {
        String result = "";

        for (ResourceTag str : tags)
            result += str.title + delimiter;

        if (!tags.isEmpty())
            result = result.substring(0, result.length() - delimiter.length());

        return result;
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
