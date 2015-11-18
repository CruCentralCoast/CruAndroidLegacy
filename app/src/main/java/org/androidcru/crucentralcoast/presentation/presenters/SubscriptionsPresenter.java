package org.androidcru.crucentralcoast.presentation.presenters;

import android.widget.ImageView;

import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.presentation.views.views.SubscriptionsView;

import java.util.ArrayList;

public class SubscriptionsPresenter extends MvpBasePresenter<SubscriptionsView>
{
    public ArrayList<MinistrySubscription> getMinistrySubscriptionData()
    {
        ArrayList<MinistrySubscription> subscriptions = new ArrayList<MinistrySubscription>();
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/slo-cru.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cuesta-cru.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/epic.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/destino.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/greek-row.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/athletes.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/faculty-commons.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/branded.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cuesta-north.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/hancock.png"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cru-high.png"));
        return subscriptions;
    }

}
