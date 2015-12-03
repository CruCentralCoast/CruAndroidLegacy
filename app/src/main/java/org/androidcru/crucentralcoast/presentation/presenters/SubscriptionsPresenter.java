package org.androidcru.crucentralcoast.presentation.presenters;

import android.content.Context;
import android.widget.ImageView;

import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;
import org.androidcru.crucentralcoast.presentation.views.views.SubscriptionsView;

import java.util.ArrayList;

public class SubscriptionsPresenter extends MvpBasePresenter<SubscriptionsView>
{
    public ArrayList<MinistrySubscription> getMinistrySubscriptionData(Context context)
    {
        ArrayList<MinistrySubscription> subscriptions = new ArrayList<MinistrySubscription>();
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/slo-cru.png", "slo-cru"));
        //subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cuesta-cru.png", "cuesta-cru"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/epic.png", "epic"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/destino.png", "destino"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/greek-row.png", "greek-row"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/athletes.png", "fellowship-of-christian-athletes-in-action"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/faculty-commons.png", "faculty-commons"));
        //subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/branded.png", "branded"));
        //subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cuesta-north.png", "cuesta-north"));
        //subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/hancock.png", "hancock"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cru-high.png", "cru-high"));

        for (MinistrySubscription s : subscriptions)
        {
            RegistrationIntentService.unsubscribeToMinistry(s.subscriptionSlug);
        }
        return subscriptions;
    }

}
