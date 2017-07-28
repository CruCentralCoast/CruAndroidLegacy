package com.crucentralcoast.app.presentation.views.summermissions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.SummerMission;
import com.crucentralcoast.app.data.providers.SummerMissionProvider;
import com.crucentralcoast.app.presentation.views.base.ListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class SummerMissionsFragment extends ListFragment {
    @BindView(R.id.informational_text)
    TextView informationalText;

    private RecyclerView.LayoutManager layoutManager;
    private Observer<List<SummerMission>> observer;

    public static SummerMissionsFragment newInstance() {
        return new SummerMissionsFragment();
    }

    public SummerMissionsFragment() {
        observer = createListObserver(R.layout.empty_with_alert, summerMissions -> {
            helper.recyclerView.setAdapter(new SummerMissionAdapter(getContext(), summerMissions, layoutManager));
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        informationalText.setText(R.string.empty_summer_missions);

        layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);

        helper.swipeRefreshLayout.setOnRefreshListener(this::getSummerMissions);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSummerMissions();
    }

    private void getSummerMissions() {
        helper.swipeRefreshLayout.setRefreshing(true);
        SummerMissionProvider.requestSummerMissions(this, observer);
    }
}
