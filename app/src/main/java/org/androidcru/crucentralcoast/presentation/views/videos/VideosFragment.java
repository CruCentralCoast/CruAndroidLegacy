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
import com.google.api.services.youtube.model.SearchResult;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mitch on 3/2/16.
 */
public class VideosFragment extends Fragment
{
    @Bind(R.id.video_list) RecyclerView videoList;
    @Bind(R.id.video_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_videos_view) RelativeLayout emptyVideoLayout;

    private SharedPreferences sharedPreferences;
    private LinearLayoutManager layoutManager;
    private Observer<List<SearchResult>> videoSubscriber;
    private List<SearchResult> videos;

    public VideosFragment()
    {
        videos = new ArrayList<>();
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
                Logger.e(e, "videos failed to retrieve.");
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
        sharedPreferences = CruApplication.getSharedPreferences();
        setHasOptionsMenu(true);
        layoutManager = new LinearLayoutManager(getActivity());
        videoList.setLayoutManager(layoutManager);
        VideosAdapter videosAdapter = new VideosAdapter(new ArrayList<>(), layoutManager);
        videoList.setAdapter(videosAdapter);
        // does this need the fixed size?
        swipeRefreshLayout.setColorSchemeColors(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void getCruVideos()
    {
        YouTubeVideoProvider.getInstance().requestChannelVideos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoSubscriber);
    }

    public void setVideos (List<SearchResult> cruVideos)
    {
        videos.clear();
        rx.Observable.from(cruVideos)

                .subscribeOn(Schedulers.immediate())
                .subscribe(videos::add);

        videoList.setAdapter(new VideosAdapter(videos, layoutManager));
        swipeRefreshLayout.setRefreshing(false);

        Logger.d("****Got to set videos. Size is " + cruVideos.size() + " ****");
    }

    private void forceUpdate()
    {
        YouTubeVideoProvider.getInstance().requestChannelVideos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoSubscriber);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCruVideos();
    }
}