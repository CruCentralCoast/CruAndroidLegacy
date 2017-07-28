package com.crucentralcoast.app.presentation.views.communitygroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Campus;
import com.crucentralcoast.app.data.models.MinistrySubscription;
import com.crucentralcoast.app.data.providers.SubscriptionProvider;
import com.crucentralcoast.app.presentation.views.forms.FormContentListFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.presentation.views.subscriptions.SubscriptionsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class MinistrySelectionFragment extends FormContentListFragment
{
    private GridLayoutManager layoutManager;
    private MinistrySelectionAdapter ministryAdapter;

    @BindView(R.id.informational_text) TextView informationalText;

    private Observer<HashMap<Campus, ArrayList<MinistrySubscription>>> observer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        informationalText.setText(R.string.empty_ministry_subscriptions);

        // use a grid layout manager
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                // if the element is a header, it should span the columns, otherwise it is a regular element
                return SubscriptionsAdapter.isHeader(position, ministryAdapter.ministries) ? layoutManager.getSpanCount() : 1;
            }
        });
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.swipeRefreshLayout.setOnRefreshListener(() -> getCampusMinistryMap());
    }

    public void getCampusMinistryMap()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        SubscriptionProvider.requestCampusMinistryMap(this, observer);
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        formHolder.setTitle("Join a Community Group");
        formHolder.setSubtitle("Select a ministry");
        formHolder.setNavigationVisibility(View.GONE);

        ministryAdapter = new MinistrySelectionAdapter(new HashMap<>(), formHolder);

        observer = createListObserver(R.layout.empty_with_alert,
                campusMinistryMap -> {
                    ministryAdapter = new MinistrySelectionAdapter(campusMinistryMap, formHolder);
                    helper.recyclerView.setAdapter(ministryAdapter);
                });
        getCampusMinistryMap();
    }
}
