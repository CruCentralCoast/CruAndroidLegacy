package org.androidcru.crucentralcoast.presentation.views.summermissions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.SummerMissionProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class SummerMissionsFragment extends Fragment
{
    @Bind(R.id.summer_missions_list) RecyclerView summerMissionsList;
    @Bind(R.id.progress) ProgressBar progressBar;

    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_summer_missions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getContext());
        summerMissionsList.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getSummerMissions();
    }

    private void getSummerMissions()
    {
        progressBar.setVisibility(View.VISIBLE);
        summerMissionsList.setVisibility(View.GONE);
        SummerMissionProvider.getSummerMissions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(summerMissions -> {
                    progressBar.setVisibility(View.GONE);
                    summerMissionsList.setVisibility(View.VISIBLE);
                    summerMissionsList.setAdapter(new SummerMissionAdapter(getContext(), summerMissions, layoutManager));
                });
    }
}
