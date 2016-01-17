package org.androidcru.crucentralcoast.presentation.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;
import org.androidcru.crucentralcoast.presentation.views.adapters.SubscriptionsAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends Fragment
{
    @Bind(R.id.subscription_list) RecyclerView mSubscriptionsList;

    private GridLayoutManager mLayoutManager;
    private SubscriptionsAdapter mSubscriptionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // use a grid layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mSubscriptionsList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mSubscriptionAdapter = new SubscriptionsAdapter(getMinistrySubscriptionData());
        mSubscriptionsList.setHasFixedSize(true);
        mSubscriptionsList.setAdapter(mSubscriptionAdapter);
    }


    public ArrayList<MinistrySubscription> getMinistrySubscriptionData()
    {
        // TODO Communicate with server for this list

        ArrayList<MinistrySubscription> subscriptions = new ArrayList<MinistrySubscription>();
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/slo-cru.png", "slo-cru"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cuesta-cru.png", "cru-1"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/epic.png", "epic"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/destino.png", "destino"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/greek-row.png", "greek-row"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/athletes.png", "fellowship-of-christian-athletes-in-action"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/faculty-commons.png", "faculty-commons"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/branded.png", "branded"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cuesta-north.png", "cru-2"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/hancock.png", "cru"));
        subscriptions.add(new MinistrySubscription("http://crucentralcoast.com/assets/img/landing/cru-high.png", "cru-high"));

        for (MinistrySubscription s : subscriptions)
        {
            RegistrationIntentService.unsubscribeToMinistry(s.mSubscriptionSlug);
        }
        return subscriptions;
    }
}
