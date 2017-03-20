package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.BuildConfig;
import com.crucentralcoast.app.data.models.youtube.Snippet;
import com.crucentralcoast.app.data.providers.api.YouTubeApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.providers.util.RxLoggingUtil;
import com.crucentralcoast.app.data.services.YouTubeDataService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

// Used to query the SLOCru YouTube channel for its videos
public final class YouTubeVideoProvider
{
    private boolean first;
    private String nextPageToken;
    private YouTubeDataService youTubeDataService = YouTubeApiProvider.getService();

    public YouTubeVideoProvider()
    {
        resetQuery();
    }

    public void requestVideoSearch(SubscriptionsHolder holder, Observer<List<Snippet>> observer, String search)
    {
        Subscription s = requestVideoSearch(search)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected Observable<List<Snippet>> requestVideoSearch(String search) {
        if(!first && nextPageToken == null)
            return Observable.empty();

        return youTubeDataService.getSearchResults(BuildConfig.YOUTUBEAPIKEY, "snippet", search,
                AppConstants.CRU_YOUTUBE_CHANNEL_ID, AppConstants.PAGE_SIZE, nextPageToken)
                .flatMap(response -> {
                    if(!response.items.isEmpty())
                    {
                        nextPageToken = response.nextPageToken;
                        first = false;
                        return Observable.just(response.items);
                    }
                    else
                        return Observable.empty();
                })
                .compose(RxComposeUtil.network());
    }


    public void requestChannelVideos(SubscriptionsHolder holder, Observer<List<Snippet>> observer)
    {
        Subscription s = requestChannelVideos()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public void resetQuery()
    {
        first = true;
        nextPageToken = null;
    }

    protected Observable<List<Snippet>> refreshQuery()
    {
        resetQuery();
        return requestChannelVideos();
    }

    // Returns a video response to its observer. The response contains a list of 20 videos,
    // including the videos' ids and snippets.
    public Observable<List<Snippet>> requestChannelVideos()
    {
        if(!first && nextPageToken == null)
            return Observable.empty();

        return youTubeDataService.getPlaylistVideos(BuildConfig.YOUTUBEAPIKEY, "snippet", AppConstants.CRU_YOUTUBE_UPLOADS_ID, AppConstants.PAGE_SIZE, nextPageToken)
                .flatMap(response -> {
                    if(!response.items.isEmpty())
                    {
                        nextPageToken = response.nextPageToken;
                        first = false;
                        return Observable.just(response.items);
                    }
                    else
                        return Observable.empty();
                })
                .compose(RxLoggingUtil.log("YOUTUBE_VIDEOS"))
                .compose(RxComposeUtil.network());
    }
}
