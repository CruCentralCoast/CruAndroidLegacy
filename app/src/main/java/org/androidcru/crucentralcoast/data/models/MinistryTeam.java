package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

@Parcel
public class MinistryTeam
{

    public static final String sId = "Id";
    public static final String sCruImage = "image";
    public static final String sTeamImage = "teamImage";
    public static final String sDescription = "description";
    public static final String sName = "name";
    public static final String sParentMinistryId = "parentMinistry";

    @SerializedName(sId) public String id;
    @SerializedName(sCruImage) public CruImage cruImage;
    @SerializedName(sTeamImage) public CruImage teamImage;
    @SerializedName(sDescription) public String description;
    @SerializedName(sName) public String name;
    @SerializedName(sParentMinistryId) public String parentMinistryId;

    public ArrayList<CruUser> ministryTeamLeaders;


    @ParcelConstructor
    public MinistryTeam(String id, CruImage cruImage,
                        CruImage teamImage,String name,
                        String description, String parentMinistryId)
    {
        this.id = id;
        this.cruImage = cruImage;
        this.teamImage = teamImage;
        this.description = description;
        this.name = name;
        this.parentMinistryId = parentMinistryId;
    }
}
