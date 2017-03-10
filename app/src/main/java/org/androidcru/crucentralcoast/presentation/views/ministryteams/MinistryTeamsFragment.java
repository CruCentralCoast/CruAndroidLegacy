package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.providers.MinistryTeamProvider;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class MinistryTeamsFragment extends ListFragment {
    private MinistryTeamsAdapter ministryTeamsAdapter;
    private Observer<List<MinistryTeam>> observer;

    public static MinistryTeamsFragment newInstance() {
        return new MinistryTeamsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inflateEmptyView(view, R.layout.empty_with_alert);

        super.onViewCreated(view, savedInstanceState);

        observer = createListObserver(R.layout.empty_with_alert,
                ministryTeams -> {
                    ministryTeamsAdapter = new MinistryTeamsAdapter(getActivity(), ministryTeams);
                    helper.recyclerView.setAdapter(ministryTeamsAdapter);
                });

        ministryTeamsAdapter = new MinistryTeamsAdapter(getActivity(), new ArrayList<>());

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.swipeRefreshLayout.setOnRefreshListener(() -> getMinistryTeamsList());
        getMinistryTeamsList();
    }


    public void getMinistryTeamsList() {
        helper.swipeRefreshLayout.setRefreshing(true);
        MinistryTeamProvider.requestMinistryTeams(this, observer);
    }

}
