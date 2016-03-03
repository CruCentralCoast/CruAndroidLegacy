package org.androidcru.crucentralcoast.data.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Resource
{
    @SerializedName("_id") public String id;
    @SerializedName("title") public String title;
    @SerializedName("url") public String url;
    @SerializedName("type") public ResourceType resourceType;
    public ArrayList<String> tags = new ArrayList<>();

    private final String delimiter = ", ";

    /**
     * Required for Gson/RetroFit
     */
    public Resource() {}

    public Resource(String title, String url, ResourceType resourceType, ArrayList<String> tags)
    {
        this.title = title;
        this.url = url;
        this.resourceType = resourceType;
        this.tags = tags;
    }

    public String formatTags()
    {
        String result = "";

        for (String str : tags)
            result += str + delimiter;

        if (!tags.isEmpty())
            result = result.substring(0, result.length() - delimiter.length() - 1);

        return result;
    }

    //TODO inquire about migrating resource type to individual article, videos, audio  objects
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
