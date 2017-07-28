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

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;

/**
 * Created by brittanyberlanga on 5/22/17.
 */

public class PrayerResponseListFragment extends ListFragment {

    private static final String PRAYER_REQ_ARG = "prayer_request_arg";

    private Unbinder unbinder;
    private Observer<PrayerRequest> prayerResponseSubscriber;
    private PrayerResponseAdapter prayerResponseAdapter;
    private PrayerRequest prayerRequest;

    public static PrayerResponseListFragment newInstance(PrayerRequest prayerRequest) {
        PrayerResponseListFragment fragment = new PrayerResponseListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRAYER_REQ_ARG, Parcels.wrap(prayerRequest));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prayerRequest = Parcels.unwrap(getArguments().getParcelable(PRAYER_REQ_ARG));
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
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(linearLayoutManager);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation(), getResources()
                .getDimensionPixelSize(R.dimen.activity_horizontal_margin)));
        helper.swipeRefreshLayout.setOnRefreshListener(this::getPrayerResponses);
        prayerResponseSubscriber = createListObserver(R.layout.empty_prayer_response_view,
                this::setPrayerResponses);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPrayerResponses();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getPrayerResponses() {
        helper.swipeRefreshLayout.setRefreshing(true);
        PrayerProvider.requestPrayerRequestDetails(this, prayerResponseSubscriber, prayerRequest.id,
                SharedPreferencesUtil.getLeaderAPIKey(), SharedPreferencesUtil.getFCMID());
    }

    private void setPrayerResponses(PrayerRequest prayerRequest) {
        if (prayerResponseAdapter == null) {
            prayerResponseAdapter = new PrayerResponseAdapter(new ArrayList<>());
            helper.recyclerView.setAdapter(prayerResponseAdapter);
        }
        if (prayerRequest.prayerResponses.isEmpty()) {
            onEmpty(R.layout.empty_prayer_response_view);
        }
        else {
            prayerResponseAdapter.setPrayerResponses(prayerRequest.prayerResponses);
        }
    }
}
