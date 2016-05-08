package org.androidcru.crucentralcoast.presentation.views.subscriptions;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.presentation.viewmodels.subscriptions.MinistrySubscriptionVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tbleisch on 4/28/16.
 */
public class SubscriptionsSorter {

    public static final int MINISTRY_VIEW = 0;
    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    private SubscriptionsSorter() {}

    public static boolean isHeader(int position, List<MinistrySubscriptionVM> ministries)
    {
        return position >= ministries.size() || ministries.get(position).campusName != null;
    }

    public static ArrayList<MinistrySubscriptionVM> convertSubscriptions(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        ArrayList<MinistrySubscriptionVM> ministries = new ArrayList<>();

        TreeMap<Campus, ArrayList<MinistrySubscription>> sortedCampusMinistryMap = new TreeMap<>(
                new Comparator<Campus>() {
                    public int compare(Campus c1, Campus c2) {
                        return campusMinistryMap.get(c2).size() - campusMinistryMap.get(c1).size();
                    }
                }
        );

        sortedCampusMinistryMap.putAll(campusMinistryMap);

        //adds each campus and each ministry in that campus to the ministries ArrayList in order
        for (Map.Entry<Campus, ArrayList<MinistrySubscription>> campusPair : sortedCampusMinistryMap.entrySet())
        {
            ministries.add(new MinistrySubscriptionVM(campusPair.getKey().campusName, null));
            for (MinistrySubscription m : campusPair.getValue())
                ministries.add(new MinistrySubscriptionVM(null, m));
        }

        return ministries;
    }
}
