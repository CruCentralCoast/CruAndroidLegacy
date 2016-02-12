package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.MinistryTeamProvider;
import org.androidcru.crucentralcoast.databinding.FragmentSubscriptionsBinding;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;

public class MinistryTeamsFragment extends Fragment
{
    private FragmentSubscriptionsBinding binding;

    private GridLayoutManager mLayoutManager;
    private MinistryTeamsAdapter mMinistryTeamsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscriptions, container, false);

        binding.fab.setVisibility(View.GONE);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mMinistryTeamsAdapter = new MinistryTeamsAdapter(new ArrayList<>());
        binding.subscriptionList.setHasFixedSize(true);
        binding.subscriptionList.setAdapter(mMinistryTeamsAdapter);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.subscriptionList.setLayoutManager(mLayoutManager);

        getMinistryTeamsList();
    }

    public void getMinistryTeamsList()
    {
        MinistryTeamProvider.getInstance().requestMinistryTeams()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ministryTeams -> {
                    mMinistryTeamsAdapter = new MinistryTeamsAdapter(ministryTeams);
                    binding.subscriptionList.setAdapter(mMinistryTeamsAdapter);
                });
    }

}
