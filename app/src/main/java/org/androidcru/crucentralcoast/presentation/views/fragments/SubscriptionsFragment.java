package org.androidcru.crucentralcoast.presentation.views.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.presenters.SubscriptionsPresenter;
import org.androidcru.crucentralcoast.presentation.views.adapters.SubscriptionsAdapter;
import org.androidcru.crucentralcoast.presentation.views.views.SubscriptionsView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends MvpFragment<SubscriptionsPresenter> implements SubscriptionsView
{
    @Bind(R.id.subscription_list)
    RecyclerView mSubscriptionsList;

    GridLayoutManager mLayoutManager;
    SubscriptionsAdapter mSubscriptionAdapter;

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
        mSubscriptionAdapter = new SubscriptionsAdapter(presenter.getMinistrySubscriptionData(getContext()));
        mSubscriptionsList.setHasFixedSize(true);
        mSubscriptionsList.setAdapter(mSubscriptionAdapter);
    }


    @Override
    protected SubscriptionsPresenter createPresenter()
    {
        return new SubscriptionsPresenter();
    }
}
