package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.data.providers.api.YouTubeApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;
import org.androidcru.crucentralcoast.data.services.YouTubeDataService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

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

        return youTubeDataService.getSearchResults(BuildConfig.YOUTUBEBROWSERAPIKEY, "snippet", search,
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
    protected Observable<List<Snippet>> requestChannelVideos()
    {
        if(!first && nextPageToken == null)
            return Observable.empty();

        return youTubeDataService.getPlaylistVideos(BuildConfig.YOUTUBEBROWSERAPIKEY, "snippet", AppConstants.CRU_YOUTUBE_UPLOADS_ID, AppConstants.PAGE_SIZE, nextPageToken)
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
