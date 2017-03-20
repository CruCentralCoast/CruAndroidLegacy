package com.crucentralcoast.app.presentation.views.home;

import android.support.annotation.NonNull;

import com.crucentralcoast.app.data.providers.EventProvider;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.data.providers.YouTubeVideoProvider;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class HomePresenter implements HomeContract.Presenter {

    @NonNull
    private final HomeContract.View mHomeView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    @NonNull
    private YouTubeVideoProvider mYouTubeProvider;

    private static final int NUM_ITEMS = 5;

    public HomePresenter(@NonNull HomeContract.View hubView) {
        mHomeView = hubView;
        mYouTubeProvider = new YouTubeVideoProvider();
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadEvents() {
        Subscription subscription = EventProvider.requestUsersEvents()
                .map(events -> events.subList(0, NUM_ITEMS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mHomeView::showEvents, Timber::e);
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadVideos() {
        Subscription subscription = mYouTubeProvider.requestChannelVideos()
                .map(videos -> videos.subList(0, NUM_ITEMS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mHomeView::showVideos, Timber::e);
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadRides() {
        Subscription subscription = RideProvider.requestAllRides()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mHomeView::showRides, Timber::e);
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadEvents();
        loadRides();
        loadVideos();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
