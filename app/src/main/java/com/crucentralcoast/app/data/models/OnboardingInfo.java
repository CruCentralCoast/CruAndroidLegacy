package com.crucentralcoast.app.data.models;

import com.crucentralcoast.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brittanyberlanga on 3/20/17.
 */

public class OnboardingInfo {
    public String title;
    public String description;
    public String imagePath;
    public int titleId;
    public int descriptionId;
    public int drawableId;

    public OnboardingInfo() {}

    public OnboardingInfo(int titleId, int descriptionId, int drawableId) {
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.drawableId = drawableId;
    }

    public static List<OnboardingInfo> getDefaultOnboardingInfo() {
        List<OnboardingInfo> info = new ArrayList<>();
        OnboardingInfo getConnectedInfo = new OnboardingInfo(R.string.get_connected_title,
                R.string.get_connected_desc, R.drawable.ic_group_add_grey600_144dp);
        OnboardingInfo rideInfo = new OnboardingInfo(R.string.find_a_ride_title,
                R.string.find_a_ride_desc, R.drawable.ic_car_grey600_144dp);
        info.add(getConnectedInfo);
        info.add(rideInfo);
        return info;
    }
}
