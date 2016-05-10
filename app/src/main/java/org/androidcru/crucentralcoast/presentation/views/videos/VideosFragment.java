package org.androidcru.crucentralcoast.presentation.views.videos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;
import org.androidcru.crucentralcoast.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observer;
import timber.log.Timber;

public class VideosFragment extends ListFragment
{
    private LinearLayoutManager layoutManager;
    private Observer<List<Snippet>> videoSubscriber;
    private List<Snippet> videos;
    private YouTubeVideoProvider youtubeProvider;
    private VideosAdapter videosAdapter;
    private int curSize;
    private boolean searchEnabled;
    private String searchQuery;

    public VideosFragment()
    {
        curSize = 0;
        videos = new ArrayList<>();
        youtubeProvider = new YouTubeVideoProvider();

        // Display text notifying the user if there are no videos to load, else show the videos
        videoSubscriber = new Observer<List<Snippet>>()
        {
            @Override
            public void onCompleted() {
                swipeRefreshLayout.setRefreshing(false);
                if(videos.isEmpty())
                {
                    emptyView.setVisibility(View.VISIBLE);
                }
                else
                {
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "videos failed to retrieve.");
            }

            @Override
            public void onNext(List<Snippet> searchResults)
            {
                setVideos(searchResults);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    // Inflate and set the query listener for the search bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.youtube, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                searchVideos(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchEnabled = true;
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchEnabled = false;
                forceUpdate();
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inflateEmptyView(R.layout.empty_events_view);
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Set the Recycler View to scroll so long as there are more videos that
        // can be returned by the provider.
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                if(searchEnabled)
                    youtubeProvider.requestVideoSearch(VideosFragment.this, videoSubscriber, searchQuery);
                else
                    getCruVideos();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void getCruVideos()
    {
        swipeRefreshLayout.setRefreshing(true);
        youtubeProvider.requestChannelVideos(this, videoSubscriber);
    }

    // Places the videos in the returned response into the Adapter's list of videos
    public void setVideos (List<Snippet> newVideos)
    {
        // Only set the Adapter once - on the first video request
        // Otherwise, the Adapter resets the scroll progression to the top of the list
        if(videosAdapter == null)
        {
            videosAdapter = new VideosAdapter(videos, layoutManager);
            recyclerView.setAdapter(videosAdapter);
        }
        videos.addAll(newVideos);

        // Let the Adapter know that more videos have been added to its list.
        videosAdapter.updateViewExpandedStates();
        videosAdapter.notifyDataSetChanged();
        curSize += newVideos.size();

        // Used for keeping track of the user's scroll progression through the list of videos.
        curSize += newVideos.size();
        swipeRefreshLayout.setRefreshing(false);
    }

    // Search the youtube channel for a specific video
    void searchVideos(String query) {
        videos.clear();
        curSize = 0;
        videosAdapter = null;
        youtubeProvider.resetQuery();
        youtubeProvider.requestVideoSearch(this, videoSubscriber, query);
    }

    private void forceUpdate()
    {
        // Reset the Adapter and video-related isExpanded
        videos.clear();
        curSize = 0;
        videosAdapter = null;
        swipeRefreshLayout.setRefreshing(true);
        youtubeProvider.resetQuery();
        if(searchEnabled)
            youtubeProvider.requestVideoSearch(this, videoSubscriber, searchQuery);
        else
            youtubeProvider.requestChannelVideos(this, videoSubscriber);
    }

    @Override
    public void onResume()
    {
        // TODO this sets the user back at the top of the list. Should resume at the position of where the user left the activity.
        super.onResume();
        getCruVideos();
    }
}