package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;

import java.util.ArrayList;

/**
 * Created by mitch on 5/7/16.
 */
public final class MinistryQuestion {
    public static final String sId = "_id";
    public static final String sMinistryId = "ministry";
    public static final String sQuestion = "question";
    public static final String sType = "type";
    public static final String sSelectOptions = "selectOptions";

    @SerializedName(sId) public String id;
    @SerializedName(sMinistryId) public String ministryId;
    @SerializedName(sQuestion) public String question;
    @SerializedName(sType) public String type;
    @SerializedName(sSelectOptions) public ArrayList<String> selectedOptions;

    @ParcelConstructor
    MinistryQuestion() {}
}
