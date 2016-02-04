package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class MinistryTeam
{
    @SerializedName("_id") public String mId;
    @SerializedName("image") public CruImage mCruImage;
    @SerializedName("parentMinistry") public String mParentMinistryId;

    public MinistryTeam() {}

    public MinistryTeam(String id, CruImage image, String parentMinistryId)
    {
        this.mId = id;
        this.mCruImage = image;
        this.mParentMinistryId = parentMinistryId;
    }
}
