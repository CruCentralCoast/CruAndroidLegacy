package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruImage;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
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
    @Bind(R.id.recyclerview) RecyclerView eventList;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.event_banner) ImageView eventBanner;
    //@Bind(R.id.eventName) TextView eventName;
    @Bind(R.id.ride_type) TextView rideType;
    @Bind(R.id.ride_time) TextView rideTime;
    @Bind(R.id.departureLoc) TextView departureLoc;
    @Bind(R.id.spots_remaining) TextView spotsRemaining;
    @Bind(R.id.passenger_list_heading) TextView passengerListHeading;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private MyRidesInfoAdapter rideSharingAdapter;

//    @Bind(R.id.editOffering) Button editButton;
//    @Bind(R.id.cancelOffering) Button cancelButton;

    private void setupUI(String theEventName, CruImage theImage) {
        //TODO: query for event to access event name and image
        //eventName.setText(theEventName);
        toolbar.setTitle(theEventName);
        Context context = eventBanner.getContext();
        if(theImage != null)
        {
            Picasso.with(context)
                    .load(theImage.url)
                    .fit()
                    .into(eventBanner);
        }
        rideType.setText("You are driving: " + ride.direction.getValueDetailed());
        rideTime.setText("Departure Time:\n" + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER)));
        departureLoc.setText("Pickup Location:\n" + ride.location.toString());
        spotsRemaining.setText("Spots Open: " + (ride.carCapacity - ride.passengers.size()));
        //Logger.d((ride.passengers != null) + " " + (ride.passengers.size() > 0));
        passengerListHeading.setText((ride.passengers != null && ride.passengers.size() > 0) ? "Your Passengers" : "No Passengers");
//        editButton.setOnClickListener(onEditOfferingClicked());
        initAlertDialog();
//        cancelButton.setOnClickListener(onCancelOfferingClicked());
        eventList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rideinfo);
        //Let ButterKnife find all injected views and bind them to member variables
        ButterKnife.bind(this);
        ride = Parcels.unwrap(getIntent().getExtras().getParcelable("ride"));

        getEventData();

        //LayoutManager for RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(this);
        eventList.setLayoutManager(llm);
        eventList.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));

        setAdapter();
        setSupportActionBar(toolbar);
    }

    //Adapter for RecyclerView
    private void setAdapter() {
        rideSharingAdapter = new MyRidesInfoAdapter(this, ride.passengers, ride.id);
        eventList.setAdapter(rideSharingAdapter);
        eventList.setHasFixedSize(true);
    }

    private void editMenuOption()
    {
        Intent intent = new Intent(this, DriverSignupActivity.class);
        Bundle extras = new Bundle();
        extras.putString(AppConstants.RIDE_KEY, ride.id);
        extras.putString(AppConstants.EVENT_ID, ride.eventId);
        intent.putExtras(extras);
        this.startActivity(intent);
    }

//    public View.OnClickListener onEditOfferingClicked()
//    {
//        Intent intent = new Intent(this, DriverSignupActivity.class);
//        Bundle extras = new Bundle();
//        extras.putString(AppConstants.RIDE_KEY, ride.id);
//        extras.putString(AppConstants.EVENT_ID, ride.eventId);
//        intent.putExtras(extras);
//        return v -> this.startActivity(intent);
//    }

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

//    public View.OnClickListener onCancelOfferingClicked()
//    {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.show();
//            }
//        };
//    }

    private void cancelMenuOption()
    {
        alertDialog.show();
    }

    private void getEventData()
    {
        EventProvider.requestCruEventByID(ride.eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    setupUI(result.name, result.image);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_rides_info_menu, menu);
        //LayoutInflater.from(this).inflate(R.menu.my_rides_info_menu, this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selected = (String) item.getTitle();
        switch((String) item.getTitle()) {
            case "Edit":
                editMenuOption();
                break;
            case "Cancel":
                cancelMenuOption();
                break;
            default:
                Logger.d("Incorrect item selection for action bar");
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateRide()
    {
        Logger.d("resetting ride");
        RideProvider.requestRideByID(ride.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    ride = result;
                    setAdapter();
                    spotsRemaining.setText("Spots Open: " + (ride.carCapacity - ride.passengers.size()));
                });
    }
}
