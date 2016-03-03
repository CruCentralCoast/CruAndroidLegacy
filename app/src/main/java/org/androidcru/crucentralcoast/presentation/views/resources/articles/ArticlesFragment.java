package org.androidcru.crucentralcoast.presentation.views.resources.articles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.providers.ResourceProvider;
import org.androidcru.crucentralcoast.presentation.util.CustomTabActivityHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class ArticlesFragment extends Fragment
{
    //Injected Views
    @Bind(R.id.article_list) RecyclerView articleList;
    @Bind(R.id.article_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.progress) ProgressBar progressBar;
    @Bind(R.id.empty_articles_view) LinearLayout emptyView;

    private CustomTabsIntent.Builder customTabsIntentBuilder;

    private ArrayList<Resource> resources;
    private Observer<List<Resource>> resourceSubscriber;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        resources = new ArrayList<>();

        //Let ButterKnife find all injected views and bind them to member variables
        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);

        //Update the list of resources by pulling from the server
        setupSubscriber();
        forceUpdate();

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        articleList.setLayoutManager(layoutManager);

        setupCustomTab();

        //Adapter for RecyclerView
        ArticlesAdapter articlesAdapter = new ArticlesAdapter(getActivity(), new ArrayList<>(), customTabsIntentBuilder);
        articleList.setAdapter(articlesAdapter);

        //Set up SwipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void setupSubscriber()
    {
        resourceSubscriber = new Observer<List<Resource>>()
        {
            @Override
            public void onCompleted()
            {
                if (resources.isEmpty())
                {
                    emptyView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
                else
                {
                    emptyView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e)
            {
                Logger.e(e, "Resources failed to retrieve.");
            }

            @Override
            public void onNext(List<Resource> resources)
            {
                setResources(resources);
            }
        };
    }


    private void forceUpdate()
    {
        //Start listening for stream data from network call
        this.resources.clear();
        ResourceProvider.getResourceByType(Resource.ResourceType.ARTICLE).subscribe(resourceSubscriber);
    }

    private void setResources(List<Resource> resources)
    {
        //Adapter for RecyclerView
        ArticlesAdapter articlesAdapter = new ArticlesAdapter(getActivity(), new ArrayList<>(resources), customTabsIntentBuilder);
        articleList.setAdapter(articlesAdapter);
        this.resources = new ArrayList<>(resources);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_articles, container, false);
    }

    private void setupCustomTab() {
        customTabsIntentBuilder = new CustomTabsIntent.Builder();

        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        customTabsIntentBuilder.setToolbarColor(color);

        customTabsIntentBuilder.setShowTitle(true);
    }
}
