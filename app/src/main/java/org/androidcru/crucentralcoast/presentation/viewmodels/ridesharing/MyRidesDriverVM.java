package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;

import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides.MyRidesDriverFragment;
import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import rx.observers.Observers;

public class MyRidesDriverVM {

    public Ride ride;
    public boolean isExpanded;
    private MyRidesDriverFragment parent;

    public String passengerList;
    public CruEvent cruEvent;
    private AlertDialog alertDialog;

    public MyRidesDriverVM(MyRidesDriverFragment fragment, Ride ride, boolean isExpanded)
    {
        this.ride = ride;
        this.isExpanded = isExpanded;
        this.parent = fragment;
        initAlertDialog();
        cruEvent = ride.event;
        updatePassengerList();
    }

    public String getDateTime()
    {
        return ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER));
    }

    public String getLocation() {
        return ride.location.toString();
    }

    public void updatePassengerList() {
        StringBuilder list = new StringBuilder();
        for (Passenger p : ride.passengers) {
            list.append(CruApplication.getContext().getString(R.string.myrides_passenger_list_name))
                    .append(p.name)
                    .append("\n" + CruApplication.getContext().getString(R.string.myrides_passenger_list_name) + " ")
                    .append(p.phone)
                    .append("\n\n");
        }
        passengerList = list.toString();
    }

    //Sends the info about the ride to the DriverSignupActivity so that it can fill in the data
    public View.OnClickListener onEditOfferingClicked()
    {
        Intent intent = new Intent(parent.getContext(), DriverSignupActivity.class);
        Bundle extras = new Bundle();
        //pack ride id and event
        extras.putString(AppConstants.RIDE_KEY, ride.id);
        Parcelable serializedEvent = Parcels.wrap(cruEvent);
        intent.putExtra(AppConstants.EVENT_KEY, serializedEvent);
        intent.putExtras(extras);
        return v -> parent.startActivity(intent);
    }

    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(parent.getContext()).create();
        alertDialog.setTitle(CruApplication.getContext().getString(R.string.alert_dialog_title));
        alertDialog.setMessage(CruApplication.getContext().getString(R.string.alert_dialog_driver_msg));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                CruApplication.getContext().getString(R.string.alert_dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RideProvider.dropRide(parent, Observers.create(v -> {}, e -> {}, () -> parent.forceUpdate()), ride.id);
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                CruApplication.getContext().getString(R.string.alert_dialog_no),
                new DialogInterface.OnClickListener() {
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
