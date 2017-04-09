package com.crucentralcoast.app.presentation.views.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.youtube.Snippet;
import com.crucentralcoast.app.presentation.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class HomeFragment extends Fragment implements HomeContract.View {
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.event_list)
    RecyclerView mEventList;
    @BindView(R.id.events_progress)
    ProgressBar mEventsProgress;
    @BindView(R.id.ride_list)
    RecyclerView mRideList;
    @BindView(R.id.rides_progress)
    ProgressBar mRidesProgress;
    @BindView(R.id.no_rides_view)
    TextView mNoRidesView;
    @BindView(R.id.video_list)
    RecyclerView mVideoList;
    @BindView(R.id.videos_progress)
    ProgressBar mVideosProgress;

    private HomeContract.Presenter mPresenter;
    private Unbinder mUnbinder;

    private HomeEventsAdapter mEventsAdapter;
    private HomeRidesAdapter mRidesAdapter;
    private HomeVideosAdapter mVideosAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter = new HomePresenter(this);

        LinearLayoutManager eventsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager ridesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager videosLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mEventsAdapter = new HomeEventsAdapter(getContext(), new ArrayList<>(), eventsLayoutManager);
        mRidesAdapter = new HomeRidesAdapter(new ArrayList<>());
        mVideosAdapter = new HomeVideosAdapter(getContext(), new ArrayList<>(), videosLayoutManager);

        mEventList.setAdapter(mEventsAdapter);
        mRideList.setAdapter(mRidesAdapter);
        mVideoList.setAdapter(mVideosAdapter);

        mEventList.setLayoutManager(eventsLayoutManager);
        mRideList.setLayoutManager(ridesLayoutManager);
        mVideoList.setLayoutManager(videosLayoutManager);

        mEventList.setHasFixedSize(true);
        mRideList.setHasFixedSize(true);
        mVideoList.setHasFixedSize(true);

        mEventList.setNestedScrollingEnabled(false);
        mRideList.setNestedScrollingEnabled(false);
        mVideoList.setNestedScrollingEnabled(false);

        mEventList.addItemDecoration(new HomeItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));
        mRideList.addItemDecoration(new HomeItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));
        mVideoList.addItemDecoration(new HomeItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));

        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(mPresenter::refresh);
    }

    @OnClick(R.id.more_events_button)
    public void goToEvents() {
        ((MainActivity) getActivity()).switchToEvents();
    }

    @OnClick(R.id.more_rides_button)
    public void goToRides() {
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.MY_RIDES_TAB, AppConstants.PASSENGER_TAB);
        ((MainActivity) getActivity()).switchToMyRides(bundle);
    }

    @OnClick(R.id.more_videos_button)
    public void goToVideos() {
        ((MainActivity) getActivity()).switchToVideos();
    }

    @Override
    public void showEvents(List<CruEvent> events) {
        mEventsAdapter.setEvents(events);
        mEventList.setVisibility(View.VISIBLE);
        mEventsProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRides(List<Ride> rides) {
        mRidesAdapter.setRides(rides);
        mRideList.setVisibility(View.VISIBLE);
        mRidesProgress.setVisibility(View.GONE);
    }

    @Override
    public void showVideos(List<Snippet> videos) {
        mVideosAdapter.setVideos(videos);
        mVideoList.setVisibility(View.VISIBLE);
        mVideosProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRidesCompleted() {
        if (mRidesAdapter.getRides() == null || mRidesAdapter.getRides().isEmpty()) {
            mNoRidesView.setVisibility(View.VISIBLE);
        }
        mRideList.setVisibility(View.VISIBLE);
        mRidesProgress.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.setRefreshing(true);
        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }
}
