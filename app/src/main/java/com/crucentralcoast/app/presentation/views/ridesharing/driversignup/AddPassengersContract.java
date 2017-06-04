package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.presentation.views.BasePresenter;
import com.crucentralcoast.app.presentation.views.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class AddPassengersContract {
    interface View extends BaseView<Presenter> {
        void showAvailablePassengers(List<Passenger> passengers);

        void completed();
    }

    interface Presenter extends BasePresenter {
        void loadAvailablePassengers(String eventId, Ride.Gender gender);

        void addPassengers(String rideId, List<Passenger> passengers);
    }
}
