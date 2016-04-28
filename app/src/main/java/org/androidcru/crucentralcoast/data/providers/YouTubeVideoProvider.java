package org.androidcru.crucentralcoast.data.providers;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

// Used to query the SLOCru YouTube channel for its videos
public final class YouTubeVideoProvider
{
    private YouTube.Search.List query;
    private boolean first = true;
    private String nextPageToken;

    public YouTubeVideoProvider()
    {
        YouTube youtube = new YouTube.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory.getDefaultInstance(), null)
                .setApplicationName(CruApplication.getContext().getString(R.string.app_name))
                .build();
        try
        {
            // Gets the video ids and the snippet.
            // A snippit contains the details of a search result. E.g. description, length, title
            query = youtube.search().list("id,snippet");
        }
        catch (IOException e)
        {
            Timber.e(e, "YouTubeVideoProvider error");
        }

        query.setKey(BuildConfig.YOUTUBEBROWSERAPIKEY);
        query.setChannelId(AppConstants.CRU_YOUTUBE_CHANNEL_ID);
        query.setOrder("date");
        query.setMaxResults(AppConstants.PAGE_SIZE);
        query.setType("video");
    }

    public void requestVideoSearch(SubscriptionsHolder holder, Observer<List<SearchResult>> observer, String search)
    {
        Subscription s = requestVideoSearch(search)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected Observable<List<SearchResult>> requestVideoSearch(String search) {
        if(!first && nextPageToken == null)
            return Observable.empty();

        return Observable.create(new Observable.OnSubscribe<List<SearchResult>>() {
            @Override
            public void call(Subscriber<? super List<SearchResult>> subscriber) {
                try {
                    query.setQ(search);
                    query.setPageToken(nextPageToken == null ? "" : nextPageToken);

                    SearchListResponse searchResponse = query.execute();
                    if (!searchResponse.isEmpty()) {
                        nextPageToken = searchResponse.getNextPageToken();
                        first = false;

                        subscriber.onNext(searchResponse.getItems());
                    }
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .compose(RxComposeUtil.network());
    }


    public void requestChannelVideos(SubscriptionsHolder holder, Observer<List<SearchResult>> observer)
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

    protected Observable<List<SearchResult>> refreshQuery()
    {
        resetQuery();
        return requestChannelVideos();
    }

    // Returns a video response to its observer. The response contains a list of 20 videos,
    // including the videos' ids and snippets.
    protected Observable<List<SearchResult>> requestChannelVideos()
    {
        if(!first && nextPageToken == null)
            return Observable.empty();

        return Observable.create(new Observable.OnSubscribe<List<SearchResult>>() {
            @Override
            public void call(Subscriber<? super List<SearchResult>> subscriber) {
                try {
                    query.setQ("");
                    query.setPageToken(nextPageToken == null ? "" : nextPageToken);

                    SearchListResponse searchResponse = query.execute();
                    if (!searchResponse.isEmpty()) {
                        // Save the token of the next page of the query. This will be used to get the
                        // next set of 20 items.
                        nextPageToken = searchResponse.getNextPageToken();
                        first = false;
                        subscriber.onNext(searchResponse.getItems());
                    }
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
        .compose(RxComposeUtil.network());
    }
}
