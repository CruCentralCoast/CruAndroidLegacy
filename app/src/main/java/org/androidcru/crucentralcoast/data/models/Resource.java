package org.androidcru.crucentralcoast.data.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Resource
{
    @SerializedName("_id") public String id;
    @SerializedName("name") public String title;
    @SerializedName("url") public String url;
    @SerializedName("resourceType") public ResourceType resourceType;
    @SerializedName("tags") public ArrayList<String> tags;

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
