package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.models.DatedVideo;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class FeedProvider
{
    public static void getFeedItems(SubscriptionsHolder holder, Observer<List<Dateable>> observer, YouTubeVideoProvider youTubeVideoProvider, int page, int pageSize)
    {
        Subscription s = getFeedItems(youTubeVideoProvider, page, pageSize)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Dateable>> getFeedItems(YouTubeVideoProvider youTubeVideoProvider, int page, int pageSize)
    {
        return Observable.merge(
                //events
                EventProvider.getEventsPaginated(page, pageSize)
                    .flatMap(events -> Observable.from(events)),
                //resources
                ResourceProvider.getResourcesPaginated(page, pageSize),
                //youtube videos
                (page == 0
                        ? youTubeVideoProvider.refreshQuery()
                        : youTubeVideoProvider.requestChannelVideos())
                    .flatMap(videoList -> Observable.from(videoList))
                    .map(searchResult -> new DatedVideo(searchResult)))
                //sort everything
                .toSortedList((Dateable d1, Dateable d2) -> {
                    return d2.getDate().compareTo(d1.getDate());
                })
                .compose(RxComposeUtil.network());
    }
}
