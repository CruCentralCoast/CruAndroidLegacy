package org.androidcru.crucentralcoast.presentation.views.summermissions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.androidcru.crucentralcoast.data.providers.SummerMissionProvider;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;

import java.util.List;

import rx.Observer;

public class SummerMissionsFragment extends ListFragment
{
    private RecyclerView.LayoutManager layoutManager;
    private Observer<List<SummerMission>> observer;

    public SummerMissionsFragment()
    {
        observer = new Observer<List<SummerMission>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(List<SummerMission> summerMissions)
            {
                recyclerView.setAdapter(new SummerMissionAdapter(getContext(), summerMissions, layoutManager));
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this::getSummerMissions);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getSummerMissions();
    }

    private void getSummerMissions()
    {
        swipeRefreshLayout.setRefreshing(true);
        SummerMissionProvider.requestSummerMissions(this, observer);
    }
}
