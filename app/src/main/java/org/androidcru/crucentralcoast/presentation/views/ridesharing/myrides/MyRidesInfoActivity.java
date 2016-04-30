package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruImage;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;


public class MyRidesInfoActivity extends BaseAppCompatActivity
{

    private Ride ride;

    //Injected Views
    @BindView(R.id.recyclerview) RecyclerView eventList;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.event_banner) ImageView eventBanner;
    @BindView(R.id.ride_type) TextView rideType;
    @BindView(R.id.ride_time) TextView rideTime;
    @BindView(R.id.departureLoc) TextView departureLoc;
    @BindView(R.id.spots_remaining) TextView spotsRemaining;
    @BindView(R.id.passenger_list_heading) TextView passengerListHeading;
    @BindView(R.id.toolbar) Toolbar toolbar;

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

        eventList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rideinfo);
        //Let ButterKnife find all injected views and bind them to member variables
        unbinder = ButterKnife.bind(this);
        ride = Parcels.unwrap(getIntent().getExtras().getParcelable(AppConstants.MYRIDE_RIDE_KEY));

        getEventData();

        //LayoutManager for RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(this);
        eventList.setLayoutManager(llm);
        eventList.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));

        //setup observer
        observer = new Observer<Ride>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "Rides failed to retrieve.");
            }

            @Override
            public void onNext(Ride newRide)
            {
                ride = newRide;
                setAdapter();
                spotsRemaining.setText(getString(R.string.myride_info_spots) + (ride.carCapacity - ride.passengers.size()));
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        setAdapter();
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    //Adapter for RecyclerView
    private void setAdapter() {
        rideSharingAdapter = new MyRidesInfoAdapter(this, ride.passengers, ride.id);
        eventList.setAdapter(rideSharingAdapter);
        eventList.setHasFixedSize(true);
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

    public void forceUpdate()
    {
        swipeRefreshLayout.setRefreshing(true);
        RideProvider.requestRideByID(this, observer, ride.id);
    }
}
