package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommunityGroupRequest
{
    public static final String sAnswers = "answers";

    @SerializedName(sAnswers) public ArrayList<MinistryQuestionAnswer> answers;

    public CommunityGroupRequest(ArrayList<MinistryQuestionAnswer> answers)
    {
        this.answers = answers;
    }
}
