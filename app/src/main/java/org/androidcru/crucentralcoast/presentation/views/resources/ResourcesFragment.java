package org.androidcru.crucentralcoast.presentation.views.resources;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.providers.ResourceProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

public class ResourcesFragment extends ListFragment
{
    private CustomTabsIntent.Builder customTabsIntentBuilder;

    private ArrayList<Resource> resources;
    private Observer<List<Resource>> resourceSubscriber;
    private Subscription subscription;

    public ResourcesFragment()
    {
        resources = new ArrayList<>();
        setupObserver();
    }

    void setupObserver()
    {
        resourceSubscriber = new Observer<List<Resource>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);

                if (resources.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "Resources failed to retrieve.");
            }

            @Override
            public void onNext(List<Resource> resources)
            {
                    setResources(resources);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        inflateEmptyView(R.layout.empty_articles_view);
        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout
        super.onViewCreated(view, savedInstanceState);

        //Update the list of resources by pulling from the server
        forceUpdate();

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void forceUpdate()
    {
        swipeRefreshLayout.setRefreshing(true);
        //Start listening for stream data from network call
        this.resources.clear();
        ResourceProvider.findResources(this, resourceSubscriber, Resource.ResourceType.values(), null);
    }

    private void setResources(List<Resource> resources)
    {
        //Adapter for RecyclerView
        ResourcesAdapter resourcesAdapter = new ResourcesAdapter(new ArrayList<>(resources), customTabsIntentBuilder);
        recyclerView.setAdapter(resourcesAdapter);
        this.resources = new ArrayList<>(resources);
    }
}
