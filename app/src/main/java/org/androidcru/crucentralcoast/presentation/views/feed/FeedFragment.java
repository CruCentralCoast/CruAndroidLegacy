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

import rx.Observer;
import timber.log.Timber;

public class FeedFragment extends ListFragment
{
    private LinearLayoutManager layoutManager;
    private Observer<List<Dateable>> observer;
    private List<Dateable> items;
    private FeedAdapter adapter;

    private YouTubeVideoProvider youTubeVideoProvider;

    public FeedFragment()
    {
        youTubeVideoProvider = new YouTubeVideoProvider();
        items = new ArrayList<>();
        observer = new Observer<List<Dateable>>()
        {
            @Override
            public void onCompleted()
            {
                if(items.isEmpty())
                {
                    emptyView.setVisibility(View.VISIBLE);
                    ((TextView) (FeedFragment.this.getView().findViewById(R.id.informational_text))).setText(R.string.no_feed_items);
                }
                else
                {
                    emptyView.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "Feed Error");
            }

            @Override
            public void onNext(List<Dateable> dateables)
            {
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
        inflateEmptyView(R.layout.empty_with_alert);

        super.onViewCreated(view, savedInstanceState);

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
