package org.androidcru.crucentralcoast.presentation.views.hub;

import android.support.annotation.NonNull;

import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class HubPresenter implements HubContract.Presenter {

    @NonNull
    private final HubContract.View mHubView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    @NonNull
    private YouTubeVideoProvider mYouTubeProvider;

    private static final int NUM_ITEMS = 5;

    public HubPresenter(@NonNull HubContract.View hubView) {
        mHubView = hubView;
        mYouTubeProvider = new YouTubeVideoProvider();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadEvents() {
        Subscription subscription = EventProvider.requestUsersEvents()
                .map(events -> events.subList(0, NUM_ITEMS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mHubView::showEvents, Timber::e);
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadVideos() {
        Subscription subscription = mYouTubeProvider.requestChannelVideos()
                .map(videos -> videos.subList(0, NUM_ITEMS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mHubView::showVideos, Timber::e);
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadEvents();
        loadVideos();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
