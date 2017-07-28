package com.crucentralcoast.app.presentation.views.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Dateable;
import com.crucentralcoast.app.data.providers.FeedProvider;
import com.crucentralcoast.app.data.providers.YouTubeVideoProvider;
import com.crucentralcoast.app.presentation.views.base.ListFragment;
import com.crucentralcoast.app.util.EndlessRecyclerViewScrollListener;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class FeedFragment extends ListFragment {
    private StaggeredGridLayoutManager layoutManager;
    private Observer<List<Dateable>> observer;
    private FeedAdapter adapter;

    private YouTubeVideoProvider youTubeVideoProvider;
    @BindView(R.id.informational_text)
    TextView informationalText;

    private static final int SPAN_COUNT = 2;

    public FeedFragment() {
        youTubeVideoProvider = new YouTubeVideoProvider();

        observer = createListObserver(
                dateables -> {
                    if (adapter.getRawItems().isEmpty()) {
                        adapter.setRawItems(dateables);
                    }
                    else {
                        adapter.addAllRawItems(dateables);
                    }
                    adapter.syncItems();
                },
                () -> {
                    if (adapter.getRawItems() == null || adapter.getRawItems().isEmpty()) {
                        onEmpty(R.layout.empty_with_alert);
                    }
                }
        );
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
        informationalText.setText(R.string.no_feed_items);

        layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        helper.recyclerView.addItemDecoration(new SpacesItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.item_spacing)));
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.recyclerView.setHasFixedSize(true);
        helper.recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getMoreFeedItems(page);
            }
        });

        adapter = new FeedAdapter(new ArrayList<>(), layoutManager);
        helper.recyclerView.setAdapter(adapter);

        helper.swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);

        helper.swipeRefreshLayout.setRefreshing(true);
        forceUpdate();
    }

    private void forceUpdate() {
        adapter.getRawItems().clear();
        getMoreFeedItems(0);
    }

    private void getMoreFeedItems(int page) {
        helper.swipeRefreshLayout.setRefreshing(true);
        FeedProvider.getFeedItems(this, observer, youTubeVideoProvider, ZonedDateTime.now(),
                SharedPreferencesUtil.getLeaderAPIKey(), page, (int) AppConstants.PAGE_SIZE);
    }
}
