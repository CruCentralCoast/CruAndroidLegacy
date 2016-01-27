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
import org.androidcru.crucentralcoast.data.providers.CruServiceProvider;
import org.androidcru.crucentralcoast.presentation.views.adapters.SubscriptionsAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


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

    private Subscriber<ArrayList<MinistrySubscription>> mSubscriber;

    public SubscriptionsFragment()
    {
        mSubscriber = new Subscriber<ArrayList<MinistrySubscription>>()
        {
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(ArrayList<MinistrySubscription> ministrySubscriptions)
            {
                mSubscriptionAdapter = new SubscriptionsAdapter(ministrySubscriptions);
                mSubscriptionsList.setAdapter(mSubscriptionAdapter);
            }
        };
    }

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
        mSubscriptionAdapter = new SubscriptionsAdapter(new ArrayList<>());
        mSubscriptionsList.setHasFixedSize(true);
        mSubscriptionsList.setAdapter(mSubscriptionAdapter);

        getMinistrySubscriptionData();
    }


    public void getMinistrySubscriptionData()
    {
        CruServiceProvider.getInstance().requestMinistries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }
}
