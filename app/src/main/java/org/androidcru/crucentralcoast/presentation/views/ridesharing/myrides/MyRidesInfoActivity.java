package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by main on 2/27/2016.
 */
public class MyRidesInfoActivity extends AppCompatActivity {

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    private LinearLayoutManager layoutManager;
//    private Observer<List<Passenger>> passengerSubscriber;
//    private ArrayList<Passenger> passengers;
    private Ride ride;

    //Injected Views
    @Bind(R.id.event_list) RecyclerView eventList;
    @Bind(R.id.event_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.eventName) TextView eventName;
    @Bind(R.id.ride_time) TextView rideTime;
    @Bind(R.id.departureLoc) TextView departureLoc;

    @Bind(R.id.editOffering) Button editButton;
    @Bind(R.id.cancelOffering) Button cancelButton;

    private void setupUI() {
        //TODO: query for event to access event name and image
        eventName.setText(ride.eventId);
        rideTime.setText(ride.time.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));
        departureLoc.setText(ride.location.toString());
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
        layoutManager = new LinearLayoutManager(this);
        eventList.setLayoutManager(layoutManager);

        //Adapter for RecyclerView
        MyRidesInfoAdapter rideSharingAdapter = new MyRidesInfoAdapter(this, ride.passengers);
        eventList.setAdapter(rideSharingAdapter);
        eventList.setHasFixedSize(true);
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
