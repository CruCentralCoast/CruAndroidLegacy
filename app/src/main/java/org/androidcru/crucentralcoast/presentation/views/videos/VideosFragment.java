package org.androidcru.crucentralcoast.presentation.views.videos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.api.services.youtube.model.SearchResult;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;
import org.androidcru.crucentralcoast.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

public class VideosFragment extends BaseSupportFragment
{
    @Bind(R.id.video_list) RecyclerView videoList;
    @Bind(R.id.video_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_videos_view) RelativeLayout emptyVideoLayout;

    private LinearLayoutManager layoutManager;
    private Observer<List<SearchResult>> videoSubscriber;
    private Subscription subscription;
    private List<SearchResult> videos;
    // used for filtering duplicate videos before being passed to the adapter
    private List<SearchResult> tempVideos;
    private YouTubeVideoProvider youtubeProvider;
    private VideosAdapter videosAdapter;
    private int curSize;

    public VideosFragment()
    {
        curSize = 0;
        videos = new ArrayList<>();
        tempVideos = new ArrayList<>();
        youtubeProvider = new YouTubeVideoProvider();

        // Display text notifying the user if there are no videos to load, else show the videos
        videoSubscriber = new Observer<List<SearchResult>>()
        {
            @Override
            public void onCompleted() {
                if(videos.isEmpty())
                {
                    swipeRefreshLayout.setVisibility(View.GONE);
                    emptyVideoLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    emptyVideoLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "videos failed to retrieve.");
            }

            @Override
            public void onNext(List<SearchResult> searchResults)
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
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
        layoutManager = new LinearLayoutManager(getActivity());
        videoList.setLayoutManager(layoutManager);

        // Set the Recycler View to scroll so long as there are more videos that
        // can be returned by the provider.
        videoList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                getCruVideos();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void getCruVideos()
    {
        youtubeProvider.requestChannelVideos(this, videoSubscriber);
    }

    // Places the videos in the returned response into the Adapter's list of videos
    public void setVideos (List<SearchResult> newVideos)
    {
        tempVideos.addAll(newVideos);

        // Only set the Adapter once - on the first video request
        // Otherwise, the Adapter resets the scroll progression to the top of the list
        if(videosAdapter == null)
        {
            videosAdapter = new VideosAdapter(videos, layoutManager);
            videoList.setAdapter(videosAdapter);
            videos.addAll(tempVideos);
            curSize += tempVideos.size();
        }
        else
        {
            // Don't add the video if it is already in the videos list
            for(SearchResult sr : tempVideos)
            {
                if(!videos.contains(sr))
                {
                    videos.add(sr);
                    ++curSize;
                }
            }

            // Let the Adapter know that more videos have been added to its list.
            videosAdapter.notifyItemChanged(curSize, videosAdapter.getItemCount() - 1);
        }
        videosAdapter.updateViewExpandedStates();

        // Used for keeping track of the user's scroll progression through the list of videos.
        curSize += newVideos.size();
        swipeRefreshLayout.setRefreshing(false);
        tempVideos.clear();
    }

    private void forceUpdate()
    {
        // Reset the Adapter and video-related isExpanded
        videos.clear();
        curSize = 0;
        videosAdapter = null;

        youtubeProvider.resetQuery();
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