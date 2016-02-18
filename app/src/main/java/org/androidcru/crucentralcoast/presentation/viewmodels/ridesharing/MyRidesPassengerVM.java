package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.Activity;
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
    public String getPassengerList() {
        String list = "";

        for (String passenger : ride.passengers)
        {
            list += passenger + "\n";
        }

        return list;
    }

    //TODO:put this somewhere else, like in strings.xml
    private static final String RIDE_KEY = "filled ride";

    //Sends the info about the ride to the DriverSignupActivity so that it can fill in the data
    public View.OnClickListener onEditOfferingClicked()
    {
        Intent intent = new Intent(parent, DriverSignupActivity.class);
        Bundle extras = new Bundle();
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(ride.driverNumber);
        temp.add(ride.eventId);
        extras.putStringArrayList(RIDE_KEY, temp);
        intent.putExtras(extras);
        return v -> parent.startActivity(intent);
    }

    //TODO: change thissssss
    public View.OnClickListener onCancelOfferingClicked()
    {
        return v -> parent.startActivity(new Intent(parent, DriverSignupActivity.class));
    }
}
