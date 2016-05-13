package org.androidcru.crucentralcoast.presentation.views.subscriptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.providers.SubscriptionProvider;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.viewmodels.subscriptions.MinistrySubscriptionVM;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;

/**
 * @author Connor Batch
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends BaseSupportFragment
{
    private GridLayoutManager layoutManager;
    private SubscriptionsAdapter subscriptionAdapter;

    @BindView(R.id.subscription_list) RecyclerView subscriptionList;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.progress) ProgressBar progressBar;

    private Subscription subscription;
    private Observer<HashMap<Campus, ArrayList<MinistrySubscription>>> observer;

    public SubscriptionsFragment()
    {
        observer = new Observer<HashMap<Campus, ArrayList<MinistrySubscription>>>()
        {
            @Override
            public void onCompleted()
            {
                toggleProgessBar(false);
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
            {
                subscriptionAdapter = new SubscriptionsAdapter(campusMinistryMap);
                subscriptionList.setAdapter(subscriptionAdapter);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        // Sets the Floating Action Button's check icon to white
        fab.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600, android.R.color.white));

        fab.setOnClickListener(v -> {

            if (!SharedPreferencesUtil.isFirstLaunch())
            {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            getActivity().finish();
        });

        subscriptionAdapter = new SubscriptionsAdapter(new HashMap<>());
        subscriptionList.setHasFixedSize(true);
        subscriptionList.setAdapter(subscriptionAdapter);

        // use a grid layout manager
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                // if the element is a header, it should span the columns, otherwise it is a regular element
                return SubscriptionsSorter.isHeader(position, subscriptionAdapter.ministries) ? layoutManager.getSpanCount() : 1;
            }
        });
        subscriptionList.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getCampusMinistryMap();
    }

    private void toggleProgessBar(boolean isShown)
    {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
        subscriptionList.setVisibility(isShown ? View.GONE : View.VISIBLE);
        fab.setVisibility(isShown ? View.GONE : View.VISIBLE);
    }

    public void getCampusMinistryMap()
    {
        toggleProgessBar(true);
        SubscriptionProvider.requestCampusMinistryMap(this, observer);
    }
}