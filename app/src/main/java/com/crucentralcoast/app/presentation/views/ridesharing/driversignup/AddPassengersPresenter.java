package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import com.crucentralcoast.app.data.providers.RideProvider;

import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class AddPassengersPresenter implements AddPassengersContract.Presenter {

    private AddPassengersContract.View mAddPassengersView;

    public AddPassengersPresenter(AddPassengersContract.View addPassengersView) {
        mAddPassengersView = addPassengersView;
        mAddPassengersView.setPresenter(this);
    }

    @Override
    public void loadAvailablePassengers(String eventId) {
        RideProvider.getAvailablePassengers(eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mAddPassengersView::showAvailablePassengers,
                        Timber::e,
                        () -> Timber.i("Successfully got available passengers")
                );
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
