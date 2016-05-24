package org.androidcru.crucentralcoast.presentation.views.subscriptions;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tbleisch on 4/28/16.
 */
public class SubscriptionsSorter {

    private SubscriptionsSorter() {}

    public static ArrayList<Item<Campus, MinistrySubscription>> convertSubscriptions(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        ArrayList<Item<Campus, MinistrySubscription>> ministries = new ArrayList<>();

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
            ministries.add(new Item(campusPair.getKey(), null));
            for (MinistrySubscription m : campusPair.getValue())
                ministries.add(new Item(null, m));
        }

        return ministries;
    }
}
