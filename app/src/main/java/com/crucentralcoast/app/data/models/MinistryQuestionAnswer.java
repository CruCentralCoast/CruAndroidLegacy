package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class MinistryQuestionAnswer
{
    public static final String sId = "_id";
    public static final String sQuestion = "question";
    public static final String sValue = "value";

    @SerializedName(sId) public String id;
    @SerializedName(sQuestion) public String question;
    @SerializedName(sValue) public String answer;

    public MinistryQuestion ministryQuestion;

    @ParcelConstructor
    public MinistryQuestionAnswer(MinistryQuestion ministryQuestion, String answer)
    {
        this.ministryQuestion = ministryQuestion;
        this.question = ministryQuestion.id;
        this.answer = answer;
    }
}
