package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.MinistryTeamProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class MinistryTeamsFragment extends Fragment
{
    @Bind(R.id.subscription_list) RecyclerView subscriptionList;

    private GridLayoutManager mLayoutManager;
    private MinistryTeamsAdapter mMinistryTeamsAdapter;

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
        mMinistryTeamsAdapter = new MinistryTeamsAdapter(getActivity(), new ArrayList<>());
        subscriptionList.setHasFixedSize(true);
        subscriptionList.setAdapter(mMinistryTeamsAdapter);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        subscriptionList.setLayoutManager(mLayoutManager);

        getMinistryTeamsList();
    }

    public void getMinistryTeamsList()
    {
        MinistryTeamProvider.requestMinistryTeams()
                .compose(RxLoggingUtil.log("MINISTRY TEAMS"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ministryTeams -> {
                    mMinistryTeamsAdapter = new MinistryTeamsAdapter(getActivity(), ministryTeams);
                    subscriptionList.setAdapter(mMinistryTeamsAdapter);
                });
    }

}
