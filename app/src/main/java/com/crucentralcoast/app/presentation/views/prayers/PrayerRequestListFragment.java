package com.crucentralcoast.app.presentation.views.prayers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;
import com.crucentralcoast.app.data.providers.PrayerProvider;
import com.crucentralcoast.app.presentation.util.DividerItemDecoration;
import com.crucentralcoast.app.presentation.views.base.ListFragment;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;

/**
 * Created by brittanyberlanga on 5/13/17.
 */

public class PrayerRequestListFragment extends ListFragment {

    private static final String SHOW_MY_REQ_ARG = "show_my_requests";
    private boolean showMyRequests;
    private Unbinder unbinder;
    private Observer<List<PrayerRequest>> prayerRequestSubscriber;
    private PrayerRequestAdapter prayerRequestAdapter;

    public static PrayerRequestListFragment newInstance(boolean showMyRequests) {
        PrayerRequestListFragment fragment = new PrayerRequestListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_MY_REQ_ARG, showMyRequests);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showMyRequests = getArguments().getBoolean(SHOW_MY_REQ_ARG, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflateEmptyView(view, R.layout.empty_with_alert);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(linearLayoutManager);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation(), getResources()
                .getDimensionPixelSize(R.dimen.activity_horizontal_margin)));
        helper.swipeRefreshLayout.setOnRefreshListener(this::getPrayerRequests);

        prayerRequestSubscriber = createListObserver(showMyRequests ?
                        R.layout.empty_my_prayer_requests_view :
                        R.layout.empty_prayer_requests_view,
                this::setPrayerRequests);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPrayerRequests();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getPrayerRequests() {
        helper.swipeRefreshLayout.setRefreshing(true);
        if (showMyRequests) {
            PrayerProvider.requestMyPrayerRequests(this, prayerRequestSubscriber,
                    SharedPreferencesUtil.getFCMID());
        } else {
            PrayerProvider.requestPrayerRequests(this, prayerRequestSubscriber,
                    SharedPreferencesUtil.getLeaderAPIKey(), SharedPreferencesUtil.getUserId());
        }
    }

    private void setPrayerRequests(List<PrayerRequest> prayerRequests) {
        if (prayerRequestAdapter == null) {
            prayerRequestAdapter = new PrayerRequestAdapter(new ArrayList<>());
            helper.recyclerView.setAdapter(prayerRequestAdapter);
        }
        prayerRequestAdapter.setPrayerRequests(prayerRequests);
    }
}
