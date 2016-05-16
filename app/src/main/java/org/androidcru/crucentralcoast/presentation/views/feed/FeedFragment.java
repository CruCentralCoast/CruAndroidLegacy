package org.androidcru.crucentralcoast.presentation.views.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.providers.FeedProvider;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;
import org.androidcru.crucentralcoast.util.EndlessRecyclerViewScrollListener;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class FeedFragment extends ListFragment
{
    private LinearLayoutManager layoutManager;
    private Observer<List<Dateable>> observer;
    private List<Dateable> items;
    private FeedAdapter adapter;

    private YouTubeVideoProvider youTubeVideoProvider;
    @BindView(R.id.informational_text) TextView informationalText;

    public FeedFragment()
    {
        youTubeVideoProvider = new YouTubeVideoProvider();
        items = new ArrayList<>();

        observer = createListObserver(
                (dateables) -> {
                    if(items == null || items.isEmpty())
                    {
                        items = dateables;
                        adapter = new FeedAdapter(dateables, layoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                    else
                    {
                        items.addAll(dateables);
                        adapter.syncItems();
                    }
                },
                () -> {
                    if(items == null || items.isEmpty())
                    {
                        onEmpty(R.layout.empty_with_alert);
                    }
                }
        );
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
        inflateEmptyView(R.layout.empty_with_alert);

        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        informationalText.setText(R.string.no_feed_items);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
             @Override
             public void onLoadMore(int page, int totalItemsCount)
             {
                 getMoreFeedItems(page);
             }
         });

        swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        forceUpdate();
    }

    private void forceUpdate()
    {
        items.clear();
        adapter = null;

        getMoreFeedItems(0);
    }

    private void getMoreFeedItems(int page)
    {
        swipeRefreshLayout.setRefreshing(true);
        FeedProvider.getFeedItems(this, observer, CruApplication.getSharedPreferences(), youTubeVideoProvider, ZonedDateTime.now(), page, (int) AppConstants.PAGE_SIZE);
    }
}
