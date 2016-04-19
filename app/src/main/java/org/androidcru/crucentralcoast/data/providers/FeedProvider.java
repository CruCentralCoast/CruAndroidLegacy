package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.threeten.bp.ZonedDateTime;

import rx.Observable;
import rx.Observer;

public class FeedProvider
{
    public static void getFeedItems(SubscriptionsHolder holder, Observer<Dateable> observer, ZonedDateTime minDate)
    {

    }

    protected static Observable<Dateable> getFeedItems(ZonedDateTime minDate)
    {

    }
}
