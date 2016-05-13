package org.androidcru.crucentralcoast.presentation.viewmodels.subscriptions;

import android.content.SharedPreferences;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

/**
 * @author Connor Batch
 */
public class MinistrySubscriptionVM
{
    public String campusName;
    public MinistrySubscription ministry;
    private Boolean isSubscribed;

    public MinistrySubscriptionVM(String campusName, MinistrySubscription ministry)
    {
        this.campusName = campusName;
        this.ministry = ministry;
    }


    public boolean getIsSubscribed()
    {
        if(isSubscribed == null)
            isSubscribed = SharedPreferencesUtil.getMinistrySubscriptionIsSubscribed(CruApplication.getContext(), ministry.subscriptionId);
        return isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed)
    {
        this.isSubscribed = isSubscribed;
        if(isSubscribed)
            RegistrationIntentService.subscribeToMinistry(ministry.subscriptionId);
        else
            RegistrationIntentService.unsubscribeToMinistry(ministry.subscriptionId);

        SharedPreferencesUtil.writeMinistrySubscriptionIsSubscribed(CruApplication.getContext(), ministry.subscriptionId, isSubscribed);
    }
}
