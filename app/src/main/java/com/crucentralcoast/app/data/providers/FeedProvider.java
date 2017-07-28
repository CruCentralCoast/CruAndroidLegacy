package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.Dateable;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class FeedProvider
{

    public static <T extends Dateable> Observable.Transformer<T, T> getSortDateable() {
        return tObservable ->
              tObservable
                      .toSortedList((T d1, T d2) -> {
                          if (d2.getDate() == null && d1.getDate() == null) {
                              return 0;
                          }
                          else if (d2.getDate() == null) {
                              return -1;
                          }
                          else if (d1.getDate() == null){
                              return 1;
                          }
                          else {
                              return d2.getDate().compareTo(d1.getDate());
                          }
                      })
                      .flatMap(Observable::from);
    }

    public static void getFeedItems(SubscriptionsHolder holder, Observer<List<Dateable>> observer,
                                    YouTubeVideoProvider youTubeVideoProvider, ZonedDateTime fromDate, String leaderAPIKey, int page, int pageSize)
    {
        Subscription s = getFeedItems(youTubeVideoProvider, fromDate, leaderAPIKey, page, pageSize)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Dateable>> getFeedItems(YouTubeVideoProvider youTubeVideoProvider,
                                                             ZonedDateTime fromDate, String leaderAPIKey, int page, int pageSize)
    {
        return Observable.merge(
                //events
                EventProvider.getEventsPaginated(fromDate, page, pageSize)
                    .flatMap(Observable::from)
                    .compose(EventProvider.getSubscriptionFilter()),
                //resources
                ResourceProvider.getResourcesPaginated(page, pageSize, leaderAPIKey),
                //youtube videos
                (page == 0
                        ? youTubeVideoProvider.refreshQuery()
                        : youTubeVideoProvider.requestChannelVideos())
                    .flatMap(Observable::from)
                )
                //sort everything
                .compose(getSortDateable())
                .compose(RxComposeUtil.toListOrEmpty())
                .compose(RxComposeUtil.network());
    }
}
