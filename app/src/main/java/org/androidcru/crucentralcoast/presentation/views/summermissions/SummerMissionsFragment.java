package org.androidcru.crucentralcoast.presentation.views.summermissions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.SummerMissionProvider;
import org.androidcru.crucentralcoast.presentation.views.ListFragment;

import rx.android.schedulers.AndroidSchedulers;

public class SummerMissionsFragment extends ListFragment
{
    private RecyclerView.LayoutManager layoutManager;

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

        SummerMissionProvider.getSummerMissions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(summerMissions -> {
                    recyclerView.setAdapter(new SummerMissionAdapter(getContext(), summerMissions, layoutManager));
                }, e -> {}, () -> {
                    swipeRefreshLayout.setRefreshing(false);
                });
    }
}
