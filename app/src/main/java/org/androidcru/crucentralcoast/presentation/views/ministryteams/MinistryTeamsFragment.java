package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.MinistryTeamProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class MinistryTeamsFragment extends Fragment
{
    @Bind(R.id.subscription_list) RecyclerView mMinistryTeamsList;

    // elements to set to gone
    @Bind(R.id.fab) FloatingActionButton mFab;

    private GridLayoutManager mLayoutManager;
    private MinistryTeamsAdapter mMinistryTeamsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        ButterKnife.bind(this, view);

        // set subscription specific elements to be gone
        mFab.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mMinistryTeamsAdapter = new MinistryTeamsAdapter(new ArrayList<>());
        mMinistryTeamsList.setHasFixedSize(true);
        mMinistryTeamsList.setAdapter(mMinistryTeamsAdapter);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mMinistryTeamsList.setLayoutManager(mLayoutManager);

        getMinistryTeamsList();
    }

    public void getMinistryTeamsList()
    {
        MinistryTeamProvider.getInstance().requestMinistryTeams()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ministryTeams -> {
                    mMinistryTeamsAdapter = new MinistryTeamsAdapter(ministryTeams);
                    mMinistryTeamsList.setAdapter(mMinistryTeamsAdapter);
                });
    }

}
