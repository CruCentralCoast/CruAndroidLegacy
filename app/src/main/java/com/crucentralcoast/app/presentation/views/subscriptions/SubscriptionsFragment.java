package com.crucentralcoast.app.presentation.views.subscriptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Campus;
import com.crucentralcoast.app.data.models.MinistrySubscription;
import com.crucentralcoast.app.data.providers.SubscriptionProvider;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.views.MainActivity;
import com.crucentralcoast.app.presentation.views.base.ListFragment;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * @author Connor Batch
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends ListFragment
{
    private GridLayoutManager layoutManager;
    private SubscriptionsAdapter subscriptionAdapter;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.informational_text) TextView informationalText;

    private Observer<HashMap<Campus, ArrayList<MinistrySubscription>>> observer;

    public SubscriptionsFragment()
    {
        observer = createListObserver(
                campusMinistryMap -> {
                    subscriptionAdapter = new SubscriptionsAdapter(campusMinistryMap);
                    helper.recyclerView.setAdapter(subscriptionAdapter);
                },
                () -> {
                    fab.setVisibility(View.GONE);
                    onEmpty(R.layout.empty_with_alert);
                },
                () -> {
                    fab.setVisibility(View.GONE);
                    onNoNetwork();
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        informationalText.setText(R.string.empty_ministry_subscriptions);

        // Sets the Floating Action Button's check icon to white
        fab.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600, android.R.color.white));
        fab.setOnClickListener(v -> {

            if (!SharedPreferencesUtil.isFirstLaunch())
            {
                SharedPreferencesUtil.writeFirstLaunch(true);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            getActivity().finish();
        });

        // use a grid layout manager
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                // if the element is a header, it should span the columns, otherwise it is a regular element
                return SubscriptionsAdapter.isHeader(position, subscriptionAdapter.ministries) ? layoutManager.getSpanCount() : 1;
            }
        });
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.swipeRefreshLayout.setOnRefreshListener(() -> getCampusMinistryMap());
        getCampusMinistryMap();

    }

    public void getCampusMinistryMap()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        SubscriptionProvider.requestCampusMinistryMap(this, observer);
    }

    @Override
    public void showContent()
    {
        super.showContent();
        fab.setVisibility(View.VISIBLE);
    }
}