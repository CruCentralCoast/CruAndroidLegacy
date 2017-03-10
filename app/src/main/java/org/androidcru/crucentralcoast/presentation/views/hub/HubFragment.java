package org.androidcru.crucentralcoast.presentation.views.hub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class HubFragment extends Fragment implements HubContract.View {
    @BindView(R.id.event_list)
    RecyclerView mEventList;
    @BindView(R.id.video_list)
    RecyclerView mVideoList;

    private HubContract.Presenter mPresenter;
    private Unbinder mUnbinder;

    private HubEventsAdapter mEventsAdapter;
    private HubVideosAdapter mVideosAdapter;

    public static HubFragment newInstance() {
        return new HubFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_hub, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter = new HubPresenter(this);

        LinearLayoutManager eventsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager videosLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mEventsAdapter = new HubEventsAdapter(getContext(), new ArrayList<>(), eventsLayoutManager);
        mVideosAdapter = new HubVideosAdapter(getContext(), new ArrayList<>(), videosLayoutManager);

        mEventList.setAdapter(mEventsAdapter);
        mVideoList.setAdapter(mVideosAdapter);

        mEventList.setLayoutManager(eventsLayoutManager);
        mVideoList.setLayoutManager(videosLayoutManager);

        mEventList.setHasFixedSize(true);
        mVideoList.setHasFixedSize(true);

        mEventList.setNestedScrollingEnabled(false);
        mVideoList.setNestedScrollingEnabled(false);

        mEventList.addItemDecoration(new HubItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));
        mVideoList.addItemDecoration(new HubItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));
    }

    @OnClick(R.id.more_events_button)
    public void goToEvents() {
        ((MainActivity) getActivity()).switchToEvents();
    }

    @OnClick(R.id.more_videos_button)
    public void goToVideos() {
        ((MainActivity) getActivity()).switchToVideos();
    }

    @Override
    public void showEvents(List<CruEvent> events) {
        mEventsAdapter.setEvents(events);
    }

    @Override
    public void showVideos(List<Snippet> videos) {
        mVideosAdapter.setVideos(videos);
    }

    @Override
    public void setPresenter(HubContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }
}
