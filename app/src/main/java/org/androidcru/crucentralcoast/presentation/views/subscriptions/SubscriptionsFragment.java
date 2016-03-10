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
import org.androidcru.crucentralcoast.presentation.views.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends Fragment
{

    private GridLayoutManager layoutManager;
    private SubscriptionsAdapter subscriptionAdapter;

    @Bind(R.id.subscription_list) RecyclerView subscriptionList;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.progress) ProgressBar progressBar;

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
            public void onError(Throwable e)
            {

            }

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
        ButterKnife.bind(this, view);

        fab.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600_48dp, android.R.color.white));
        fab.setOnClickListener(v -> {

            if (!CruApplication.getSharedPreferences().getBoolean(AppConstants.FIRST_LAUNCH, false))
            {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            CruApplication.getSharedPreferences().edit().putBoolean(AppConstants.FIRST_LAUNCH, true).apply();
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
                return subscriptionAdapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(subscription != null)
            subscription.unsubscribe();
    }

    private void toggleProgessBar(boolean isShown)
    {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
        subscriptionList.setVisibility(isShown ? View.GONE : View.VISIBLE);
        fab.setVisibility(isShown ? View.GONE : View.VISIBLE);
    }

    public void getCampusMinistryMap()
    {
        if(subscription != null)
            subscription.unsubscribe();
        toggleProgessBar(true);
        subscription = SubscriptionProvider.requestCampusMinistryMap()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
