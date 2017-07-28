package com.crucentralcoast.app.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.presentation.util.DividerItemDecoration;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatListActivity;

import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;


public class MyRidesInfoActivity extends BaseAppCompatListActivity
{
    private Ride ride;

    @BindView(R.id.event_banner) ImageView eventBanner;
    @BindView(R.id.ride_type) TextView rideType;
    @BindView(R.id.ride_time) TextView rideTime;
    @BindView(R.id.departureLoc) TextView departureLoc;
    @BindView(R.id.spots_remaining) TextView spotsRemaining;
    @BindView(R.id.passenger_list_heading) TextView passengerListHeading;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private MyRidesInfoAdapter rideSharingAdapter;
    private Observer<Ride> observer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_rides_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rideinfo);
        inflateEmptyView(findViewById(android.R.id.content), R.layout.empty_with_alert);
        //Let ButterKnife find all injected views and bind them to member variables
        unbinder = ButterKnife.bind(this);
        ride = Parcels.unwrap(getIntent().getExtras().getParcelable(AppConstants.MYRIDE_RIDE_KEY));
        setupUI();

        //LayoutManager for RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(this);
        helper.recyclerView.setLayoutManager(llm);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));

        observer = helper.createListObserver(
                newRide -> {
                    ride = newRide;
                    setupPassengers();
                    setAdapter();
                    spotsRemaining.setText(getString(R.string.myride_info_spots) + " " + (ride.carCapacity - ride.passengers.size()));
                },
                () -> {});

        helper.swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
        forceUpdate();
    }

    private void setupUI() {
        toolbar.setTitle(ride.event.name);

        if(ride.event.image != null && !ride.event.image.isEmpty())
        {
            ViewUtil.setSource(eventBanner, ride.event.image, 0, null, null, ViewUtil.SCALE_TYPE.FIT);
        }
        rideType.setText(getString(R.string.myride_info_dir) + " " + ride.direction.getValueDetailed());
        rideTime.setText(getString(R.string.myride_info_departure_time)
                + "\n" + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER)));

        departureLoc.setText(getString(R.string.myride_info_pickup_loc) + "\n" + ride.location.toString());
        setupPassengers();
        spotsRemaining.setText(getString(R.string.myride_info_spots) + " " + (ride.carCapacity - ride.passengers.size()));

        helper.recyclerView.setNestedScrollingEnabled(false);
    }

    private void setupPassengers()
    {
        passengerListHeading.setText((ride.passengers != null && ride.passengers.size() > 0) ?
                getString(R.string.myride_info_passenger_list_nonempty) :
                getString(R.string.myride_info_passenger_list_empty));
    }

    //Adapter for RecyclerView
    private void setAdapter() {
        rideSharingAdapter = new MyRidesInfoAdapter(this, ride.passengers, ride.id);
        helper.recyclerView.setAdapter(rideSharingAdapter);
        helper.recyclerView.setHasFixedSize(true);
    }

    public void forceUpdate()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        RideProvider.requestRideByID(this, observer, ride.id);
    }
}
