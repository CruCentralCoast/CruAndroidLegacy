package org.androidcru.crucentralcoast.presentation.views.videos;

import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;
import org.androidcru.crucentralcoast.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideosFragment extends Fragment
{
    @Bind(R.id.video_list) RecyclerView videoList;
    @Bind(R.id.video_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_videos_view) RelativeLayout emptyVideoLayout;

    private SharedPreferences sharedPreferences;
    private LinearLayoutManager layoutManager;
    private Observer<SearchListResponse> videoSubscriber;
    private Subscription subscription;
    private List<SearchResult> videos;
    private String nextPageToken;
    private VideosAdapter videosAdapter;
    private int curSize;

    public VideosFragment()
    {
        curSize = 0;
        videos = new ArrayList<>();

        // Display text notifying the user if there are no videos to load, else show the videos
        videoSubscriber = new Observer<SearchListResponse>()
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
                Logger.e(e, "videos failed to retrieve.");
            }

            @Override
            public void onNext(SearchListResponse searchResults)
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
        sharedPreferences = CruApplication.getSharedPreferences();
        setHasOptionsMenu(true);
        layoutManager = new LinearLayoutManager(getActivity());
        videoList.setLayoutManager(layoutManager);

        // Set the Recycler View to scroll so long as there are more videos that
        // can be returned by the provider.
        videoList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                // Only query for more videos if I have more videos
                if(nextPageToken != null)
                {
                    getCruVideos(nextPageToken);
                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void getCruVideos(String nextPageToken)
    {
        if(subscription != null)
        {
            subscription.unsubscribe();
        }

        subscription = YouTubeVideoProvider.getInstance().requestChannelVideos(nextPageToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoSubscriber);
    }

    // Places the videos in the returned response into the Adapter's list of videos
    public void setVideos (SearchListResponse cruVideosResponse)
    {
        List<SearchResult> cruVideos = cruVideosResponse.getItems();
        rx.Observable.from(cruVideos)
                .subscribeOn(Schedulers.immediate())
                .subscribe(videos::add);

        // Only set the Adapter once - on the first video request
        // Otherwise, the Adapter resets the scroll progression to the top of the list
        if(videosAdapter == null)
        {
            videosAdapter = new VideosAdapter(videos, layoutManager);
            videoList.setAdapter(videosAdapter);
        }
        else
        {
            videosAdapter.updateViewExpandedStates();

            // Let the Adapter know that more videos have been added to its list.
            videosAdapter.notifyItemChanged(curSize, videosAdapter.getItemCount() - 1);
        }

        // Save the token of the next page of the query. This will be used to get the
        // next set of 20 items.
        nextPageToken = cruVideosResponse.getNextPageToken();

        // Used for keeping track of the user's scroll progression through the list of videos.
        curSize += cruVideos.size();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void forceUpdate()
    {
        // Reset the Adapter and video-related state
        videos.clear();
        curSize = 0;
        videosAdapter = null;

        YouTubeVideoProvider.getInstance().requestChannelVideos("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoSubscriber);
    }

    @Override
    public void onResume()
    {
        // TODO this sets the user back at the top of the list. Should resume at the position of where the user left the activity.
        super.onResume();
        getCruVideos("");
    }
}