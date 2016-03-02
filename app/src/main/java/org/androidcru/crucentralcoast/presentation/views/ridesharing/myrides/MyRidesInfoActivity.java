package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;


public class MyRidesInfoActivity extends AppCompatActivity {

    private Ride ride;

    private  AlertDialog alertDialog;


    //Injected Views
    @Bind(R.id.event_list) RecyclerView eventList;
    @Bind(R.id.event_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.eventName) TextView eventName;
    @Bind(R.id.ride_type) TextView rideType;
    @Bind(R.id.ride_time) TextView rideTime;
    @Bind(R.id.departureLoc) TextView departureLoc;
    @Bind(R.id.passenger_list_heading) TextView passengerListHeading;


    @Bind(R.id.editOffering) Button editButton;
    @Bind(R.id.cancelOffering) Button cancelButton;

    private void setupUI() {
        //TODO: query for event to access event name and image
        eventName.setText(ride.eventId);
        rideType.setText("Direction: " + ride.direction.getValueDetailed());
        rideTime.setText(ride.time.toString());
        rideTime.setText(ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER)));
        departureLoc.setText(ride.location.toString());
        Logger.d((ride.passengers != null) + " " + (ride.passengers.size() > 0));
                passengerListHeading.setText((ride.passengers != null && ride.passengers.size() > 0) ? "Passenger List" : "No Passengers");
        editButton.setOnClickListener(onEditOfferingClicked());
        initAlertDialog();
        cancelButton.setOnClickListener(onCancelOfferingClicked());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rideinfo);
        //Let ButterKnife find all injected views and bind them to member variables
        ButterKnife.bind(this);
        ride = Parcels.unwrap(getIntent().getExtras().getParcelable("ride"));
        setupUI();

        //Enables actions in the Activity Toolbar (top-right buttons)
        //setHasOptionsMenu(true);

        //LayoutManager for RecyclerView
        eventList.setLayoutManager(new LinearLayoutManager(this));

        //Adapter for RecyclerView
        MyRidesInfoAdapter rideSharingAdapter = new MyRidesInfoAdapter(this, ride.passengers, ride.id);
        eventList.setAdapter(rideSharingAdapter);
        eventList.setHasFixedSize(true);
    }


    public View.OnClickListener onEditOfferingClicked()
    {
        Intent intent = new Intent(this, DriverSignupActivity.class);
        Bundle extras = new Bundle();
        extras.putString(AppConstants.RIDE_KEY, ride.id);
        extras.putString(AppConstants.EVENT_ID, ride.eventId);
        intent.putExtras(extras);
        return v -> this.startActivity(intent);
    }

    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Cancel Ride");
        alertDialog.setMessage("Are you sure you want to cancel this ride?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                RideProvider.dropRide(ride.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();

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

//    public void setPassengers(List<Passenger> passengerList)
//    {
//        passengers.clear();
//        rx.Observable.from(passengerList)
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(passengers::add);
//
//        eventList.setAdapter(new MyRidesInfoAdapter(getContext(), passengers));
//        swipeRefreshLayout.setRefreshing(false);
//    }
}
