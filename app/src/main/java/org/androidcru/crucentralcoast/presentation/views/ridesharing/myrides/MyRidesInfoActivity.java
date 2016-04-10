package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.observers.Observers;


public class MyRidesInfoActivity extends BaseAppCompatActivity
{

    private Ride ride;

    private  AlertDialog alertDialog;

    //Injected Views
    @Bind(R.id.recyclerview) RecyclerView eventList;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.event_banner) ImageView eventBanner;
    @Bind(R.id.ride_type) TextView rideType;
    @Bind(R.id.ride_time) TextView rideTime;
    @Bind(R.id.departureLoc) TextView departureLoc;
    @Bind(R.id.spots_remaining) TextView spotsRemaining;
    @Bind(R.id.passenger_list_heading) TextView passengerListHeading;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private MyRidesInfoAdapter rideSharingAdapter;
    private Observer<Ride> observer;

    private void setupUI(String theEventName, CruImage theImage) {
        toolbar.setTitle(theEventName);
        Context context = eventBanner.getContext();
        if(theImage != null)
        {
            Picasso.with(context)
                    .load(theImage.url)
                    .fit()
                    .into(eventBanner);
        }
        rideType.setText(getString(R.string.myride_info_dir) + ride.direction.getValueDetailed());
        rideTime.setText(getString(R.string.myride_info_departure_time)
                + "\n" + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER)));
        departureLoc.setText(getString(R.string.myride_info_pickup_loc) + "\n" + ride.location.toString());
        spotsRemaining.setText(getString(R.string.myride_info_spots) + (ride.carCapacity - ride.passengers.size()));
        passengerListHeading.setText((ride.passengers != null && ride.passengers.size() > 0) ?
                getString(R.string.myride_info_passenger_list_nonempty) :
                getString(R.string.myride_info_passenger_list_empty));
        initAlertDialog();
        eventList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rideinfo);
        //Let ButterKnife find all injected views and bind them to member variables
        ButterKnife.bind(this);
        ride = Parcels.unwrap(getIntent().getExtras().getParcelable(AppConstants.MYRIDE_RIDE_KEY));

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
        extras.putSerializable(AppConstants.EVENT_KEY, ride.time);
        intent.putExtras(extras);
        this.startActivity(intent);
    }

    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.alert_dialog_title));
        alertDialog.setMessage(getString(R.string.alert_dialog_driver_msg));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(R.string.alert_dialog_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RideProvider.dropRide(MyRidesInfoActivity.this, Observers.empty() , ride.id);
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.alert_dialog_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
            }
                });
    }

    private void cancelMenuOption()
    {
        alertDialog.show();
    }

    private void getEventData()
    {
        EventProvider.requestCruEventByID(this, Observers.create(event -> setupUI(event.name, event.image)), ride.eventId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_rides_info_menu, menu);
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

        Observer<Ride> observer = new Observer<Ride>()
        {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Ride ride)
            {
                setAdapter();
                spotsRemaining.setText(getString(R.string.myride_info_spots) + (ride.carCapacity - ride.passengers.size()));
            }
        };

        RideProvider.requestRideByID(this, observer, ride.id);
    }
}
