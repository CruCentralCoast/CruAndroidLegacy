package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;

import java.util.ArrayList;

public final class MinistryQuestion {
    public static final String sId = "_id";
    public static final String sMinistryId = "ministry";
    public static final String sQuestion = "question";
    public static final String sType = "type";
    public static final String sSelectOptions = "selectOptions";

    @SerializedName(sId) public String id;
    @SerializedName(sMinistryId) public String ministry;
    @SerializedName(sQuestion) public String question;
    @SerializedName(sType) public Type type;
    @SerializedName(sSelectOptions) public ArrayList<Selection> selectOptions;

    @ParcelConstructor
    MinistryQuestion() {}

    public String[] getSelectionOptions()
    {
        String[] options = new String[selectOptions.size()];
        for(int i = 0; i < selectOptions.size(); i++)
        {
            options[i] = selectOptions.get(i).value;
        }
        return options;
    }

    public enum Type {
        SELECT("select"), DATETIME("datetime"), TEXT("text");

        public String value;

        Type(String string)
        {
            this.value = string;
        }
    }
}
