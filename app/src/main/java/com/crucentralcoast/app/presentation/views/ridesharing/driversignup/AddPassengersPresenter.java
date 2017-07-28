package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.PassengerProvider;
import com.crucentralcoast.app.data.providers.RideProvider;

import java.util.List;

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
    public void loadAvailablePassengers(String eventId, Ride.Gender gender, double[] location, double radius) {
        PassengerProvider.getAvailablePassengers(eventId, gender.getId(), location, radius)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mAddPassengersView::showAvailablePassengers,
                        Timber::e,
                        () -> Timber.i("Successfully got available passengers")
                );
    }

    @Override
    public void addPassengers(String rideId, List<Passenger> passengers) {
        RideProvider.addPassengersToRide(rideId, passengers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aVoid -> {},
                        Timber::e,
                        mAddPassengersView::completed
                );
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
