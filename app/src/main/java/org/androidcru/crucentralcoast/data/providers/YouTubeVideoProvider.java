package org.androidcru.crucentralcoast.data.providers;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.DatedVideo;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

// Used to query the SLOCru YouTube channel for its videos
public final class YouTubeVideoProvider
{

    private static YouTubeVideoProvider instance;

    private YouTube youtube;
    private YouTube.Search.List query;

    private YouTubeVideoProvider()
    {
        youtube = new YouTube.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory.getDefaultInstance(), null)
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
        query.setMaxResults(AppConstants.YOUTUBE_QUERY_NUM);
        query.setType("video");
    }

    public static YouTubeVideoProvider getInstance()
    {
        if(instance == null)
            instance = new YouTubeVideoProvider();
        return instance;
    }

    public void requestChannelVideos(SubscriptionsHolder holder, Observer<SearchListResponse> observer, String nextPageToken)
    {
        Subscription s = requestChannelVideos(nextPageToken)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    // Returns a video response to its observer. The response contains a list of 20 videos,
    // including the videos' ids and snippets.
    protected Observable<SearchListResponse> requestChannelVideos(String nextPageToken)
    {
        return Observable.create(new Observable.OnSubscribe<SearchListResponse>() {
            @Override
            public void call(Subscriber<? super SearchListResponse> subscriber) {
                try {

                    query.setPageToken(nextPageToken);
                    SearchListResponse searchResponse = query.execute();
                    if (!searchResponse.isEmpty()) {
                        subscriber.onNext(searchResponse);
                    }
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
        .compose(RxComposeUtil.network());
    }

    protected Observable<List<DatedVideo>> requestVideosByDate(ZonedDateTime minDate)
    {
        return Observable.create(new Observable.OnSubscribe<List<DatedVideo>>() {
            @Override
            public void call(Subscriber<? super List<DatedVideo>> subscriber) {
                try
                {
                    long time = minDate.toInstant().toEpochMilli();
                    int tzShift = (int) Duration.ofSeconds(minDate.getOffset().getTotalSeconds()).toHours();

                    Timber.d("Time: %d", time);
                    Timber.d("tzShift: %d", tzShift);

                    query.setPublishedAfter(new DateTime(time, tzShift));
                    SearchListResponse searchResponse = query.execute();

                    if (!searchResponse.isEmpty()) {
                        ArrayList<DatedVideo> datedVideos = new ArrayList<DatedVideo>();
                        for(SearchResult searchResult : searchResponse.getItems())
                        {
                            long videoTime = searchResult.getSnippet().getPublishedAt().getValue();
                            Timber.d(Long.toString(videoTime));
                            Timber.d(Boolean.toString(videoTime >= time));
                            datedVideos.add(new DatedVideo(searchResult));
                        }
                        subscriber.onNext(datedVideos);
                    }
                    subscriber.onCompleted();
                }
                catch (IOException e)
                {
                    subscriber.onError(e);
                }
            }
        })
                .compose(RxComposeUtil.network());
    }
}
