package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.androidcru.crucentralcoast.Holder;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;

@SuppressWarnings("unused")
public class MyRidesPassengerVM extends BaseObservable {

    public Ride ride;
    public final ObservableBoolean isExpanded = new ObservableBoolean();
    private Activity parent;

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";
    public ObservableField<String> passengerList;
    public ObservableField<String> eventName;
    AlertDialog alertDialog;

    public MyRidesPassengerVM(Ride ride, boolean isExpanded, Activity activity)
    {
        this.ride = ride;
        this.isExpanded.set(isExpanded);
        this.parent = activity;
        passengerList = new ObservableField<>();
        eventName = new ObservableField<>();
        initAlertDialog();
        updateEventName();
    }

    //TODO: will eventually need to get a to and from time, not just 1
    public String getDateTime()
    {
        return ride.time.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
    }

    //TODO: query for name
    public void updateEventName() {
        final Holder<String> evName = new Holder<String>();

        EventProvider.getInstance().requestCruEventByID(ride.eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    eventName.set(results.mName);
                });
    }

    public String getLocation() {
        return ride.location.toString();
    }

    //TODO: display actual info, but that'll wait for a passenger model implementation
    //TODO: also, pick some way to display the passenger stuff
    public String getDriverInfo() {
        return "Name: " + ride.driverName + "\nPhone: " + ride.driverNumber;
    }

    //TODO:put this somewhere else, like in strings.xml
    private static final String RIDE_KEY = "filled ride";

    //Sends the info about the ride to the DriverSignupActivity so that it can fill in the data
//    public View.OnClickListener onEditOfferingClicked()
//    {
//        Intent intent = new Intent(parent, DriverSignupActivity.class);
//        Bundle extras = new Bundle();
//        ArrayList<String> temp = new ArrayList<String>();
//        temp.add(ride.driverNumber);
//        temp.add(ride.eventId);
//        extras.putStringArrayList(RIDE_KEY, temp);
//        intent.putExtras(extras);
//        return v -> parent.startActivity(intent);
//    }

    private void initAlertDialog()
    {
        alertDialog = new AlertDialog.Builder(parent).create();
        alertDialog.setTitle("Cancel Ride");
        alertDialog.setMessage("Are you sure that you want to drop yourself from this ride?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RideProvider.getInstance().dropPassengerFromRide(ride.passengers.get(0), ride.id)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
            }
        });
    }

    //TODO: change thissssss
    public View.OnClickListener onCancelOfferingClicked()
    {
        return new View.OnClickListener(){
          @Override
          public void onClick(View v)
          {
              alertDialog.show();
          }
        };
    }
}
