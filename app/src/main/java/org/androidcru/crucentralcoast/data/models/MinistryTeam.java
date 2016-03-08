package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class MinistryTeam
{
    @SerializedName("_id") public String id;
    @SerializedName("image") public CruImage cruImage;
    @SerializedName("teamImage") public CruImage teamImage;
    @SerializedName("description") public String description;
    @SerializedName("name") public String name;
    @SerializedName("parentMinistry") public String parentMinistryId;
    public ArrayList<CruUser> ministryTeamLeaders;

    public MinistryTeam() {}

    public MinistryTeam(String id, CruImage image, CruImage teamImage, String name, String description, String parentMinistryId)
    {
        this.id = id;
        this.cruImage = image;
        this.teamImage = teamImage;
        this.description = description;
        this.name = name;
        this.parentMinistryId = parentMinistryId;
    }
}
