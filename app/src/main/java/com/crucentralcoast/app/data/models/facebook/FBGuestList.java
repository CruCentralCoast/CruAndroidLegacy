package com.crucentralcoast.app.data.models.facebook;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FBGuestList
{
    public @SerializedName("data") ArrayList<FBUser> data;
}
