package org.androidcru.crucentralcoast.data.providers;

import android.content.SharedPreferences;

import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class FeedProvider
{
    public static void getFeedItems(SubscriptionsHolder holder, Observer<List<Dateable>> observer,
            YouTubeVideoProvider youTubeVideoProvider, ZonedDateTime fromDate, int page, int pageSize)
    {
        Subscription s = getFeedItems(youTubeVideoProvider, fromDate, page, pageSize)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Dateable>> getFeedItems(YouTubeVideoProvider youTubeVideoProvider, ZonedDateTime fromDate, int page, int pageSize)
    {
        return Observable.merge(
                //events
                EventProvider.getEventsPaginated(fromDate, page, pageSize)
                    .flatMap(events -> Observable.from(events))
                    .compose(EventProvider.getSubscriptionFilter()),
                //resources
                ResourceProvider.getResourcesPaginated(page, pageSize),
                //youtube videos
                (page == 0
                        ? youTubeVideoProvider.refreshQuery()
                        : youTubeVideoProvider.requestChannelVideos())
                    .flatMap(videoList -> Observable.from(videoList)))
                //sort everything
                .toSortedList((Dateable d1, Dateable d2) -> {
                    return d2.getDate().compareTo(d1.getDate());
                })
                .compose(RxComposeUtil.network());
    }
}
