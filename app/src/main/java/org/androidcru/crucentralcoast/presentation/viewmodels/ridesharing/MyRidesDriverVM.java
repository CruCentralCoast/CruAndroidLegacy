package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.Holder;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides.MyRidesDriverFragment;
import org.threeten.bp.format.DateTimeFormatter;

import rx.observers.Observers;

public class MyRidesDriverVM {

    public Ride ride;
    public boolean isExpanded;
    private MyRidesDriverFragment parent;

    public String passengerList;
    public String eventName;
    AlertDialog alertDialog;

    public MyRidesDriverVM(MyRidesDriverFragment fragment, Ride ride, boolean isExpanded)
    {
        this.ride = ride;
        this.isExpanded = isExpanded;
        this.parent = fragment;
        initAlertDialog();
//        updateEventName();
        updatePassengerList();
    }

    public String getDateTime()
    {
        return ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER));
    }

    public void updateEventName() {
        final Holder<String> evName = new Holder<String>();

        EventProvider.requestCruEventByID(parent, Observers.create(results -> eventName = results.name), ride.eventId);
    }

    public String getLocation() {
        return ride.location.toString();
    }

    public void updatePassengerList() {
        StringBuilder list = new StringBuilder();
        for (Passenger p : ride.passengers) {
            if (list.toString().isEmpty())
                list.append("Name: ").append(p.name).append("\nPhone: ").append(p.phone).append("\n\n");
            else
                list.append("Name: ").append(p.name).append("\nPhone: ").append(p.phone).append("\n\n");
        }
        passengerList = list.toString();
    }

    //Sends the info about the ride to the DriverSignupActivity so that it can fill in the data
    public View.OnClickListener onEditOfferingClicked()
    {
        Intent intent = new Intent(parent.getContext(), DriverSignupActivity.class);
        Bundle extras = new Bundle();
        extras.putString(AppConstants.RIDE_KEY, ride.id);
        extras.putString(AppConstants.EVENT_ID, ride.eventId);
        intent.putExtras(extras);
        return v -> parent.startActivity(intent);
    }

    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(parent.getContext()).create();
        alertDialog.setTitle("Cancel Ride");
        alertDialog.setMessage("Are you sure you want to cancel this ride?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                RideProvider.dropRide(parent, Observers.create(v -> parent.forceUpdate()), ride.id);

            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
            }
        });
    }

    public View.OnClickListener onCancelOfferingClicked()
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        };
    }
}
