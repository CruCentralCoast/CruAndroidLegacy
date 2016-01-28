package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

public class Campus
{
    @SerializedName("_id") public String mId;
    @SerializedName("name") public String mCampusName;

    public Campus() {}

    public Campus(String mId, String mCampusName)
    {
        this.mId = mId;
        this.mCampusName = mCampusName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campus campus = (Campus) o;

        return mId.equals(campus.mId);

    }

    @Override
    public int hashCode()
    {
        return mId.hashCode();
    }
}
