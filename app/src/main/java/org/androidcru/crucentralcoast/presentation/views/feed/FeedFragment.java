package org.androidcru.crucentralcoast.presentation.views.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.providers.FeedProvider;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import rx.Observer;

public class FeedFragment extends ListFragment
{
    private RecyclerView.LayoutManager layoutManager;
    private Observer<List<Dateable>> observer;

    public FeedFragment()
    {
        observer = new Observer<List<Dateable>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(List<Dateable> dateables)
            {
                recyclerView.setAdapter(new FeedAdapter(dateables, layoutManager));
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
        FeedProvider.getFeedItems(this, observer, ZonedDateTime.now().minusWeeks(1L));
    }
}
