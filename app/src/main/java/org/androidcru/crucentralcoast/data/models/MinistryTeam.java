package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel
public final class MinistryTeam
{

    public static final String sId = "_id";
    public static final String sCruImage = "imageLink";
    public static final String sTeamImage = "teamImageLink";
    public static final String sDescription = "description";
    public static final String sName = "name";
    public static final String sParentMinistryId = "parentMinistry";
    public static final String sLeaders = "leaders";

    @SerializedName(sId) public String id;
    @SerializedName(sCruImage) public String image;
    @SerializedName(sTeamImage) public String teamImage;
    @SerializedName(sDescription) public String description;
    @SerializedName(sName) public String name;
    @SerializedName(sParentMinistryId) public String parentMinistryId;
    @SerializedName(sLeaders) public List<CruUser> ministryTeamLeaders;

    @ParcelConstructor
    MinistryTeam() {}
}
