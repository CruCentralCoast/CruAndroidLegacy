package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.providers.SubscriptionProvider;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;

public class MinistrySelectionFragment extends FormContentFragment
{
    private GridLayoutManager layoutManager;
    private MinistrySelectionAdapter ministryAdapter;

    @BindView(R.id.subscription_list) RecyclerView subscriptionList;
    @BindView(R.id.progress) ProgressBar progressBar;

    private Observer<HashMap<Campus, ArrayList<MinistrySubscription>>> observer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ministry_teams, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        subscriptionList.setHasFixedSize(true);
        subscriptionList.setAdapter(ministryAdapter);

        // use a grid layout manager
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                // if the element is a header, it should span the columns, otherwise it is a regular element
                return ministryAdapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
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
    }

    public void getCampusMinistryMap()
    {
        toggleProgessBar(true);
        SubscriptionProvider.requestCampusMinistryMap(this, observer);
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        formHolder.setTitle("Join a Community Group");
        formHolder.setSubtitle("Select a ministry");

        ministryAdapter = new MinistrySelectionAdapter(new HashMap<>(), formHolder);

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
                ministryAdapter = new MinistrySelectionAdapter(campusMinistryMap, formHolder);
                subscriptionList.setAdapter(ministryAdapter);
            }
        };
    }
}
