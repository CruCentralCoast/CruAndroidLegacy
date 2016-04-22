package org.androidcru.crucentralcoast.data.providers;

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
    public static void getFeedItems(SubscriptionsHolder holder, Observer<List<Dateable>> observer, ZonedDateTime minDate)
    {
        Subscription s = getFeedItems(minDate)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Dateable>> getFeedItems(ZonedDateTime minDate)
    {
        return Observable.merge(EventProvider.getUpcomingEvents(ZonedDateTime.now()),
                ResourceProvider.getPreviousResources(minDate),
                YouTubeVideoProvider.getInstance().requestVideosByDate(minDate)
                    .flatMap(videoList -> Observable.from(videoList)))
                    .toSortedList((Dateable d1, Dateable d2) -> {
                        return d1.getDate().compareTo(d2.getDate());
                    })
                .compose(RxComposeUtil.network());
    }
}
