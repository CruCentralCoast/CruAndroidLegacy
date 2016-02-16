package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.MapFragment;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.MyRidesDriverVM;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideVM;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.MyRidesDriverAdapter;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.threeten.bp.ZonedDateTime;

public class DriverSignupActivity extends AppCompatActivity
{
    private ActivityDriverFormBinding binding;
    private RideVM rideVM;
    private DriverSignupValidator validator;

    private FloatingActionButton fab;

    private PlaceAutocompleteFragment autocompleteFragment;
    private MapFragment mapFragment;

    //TODO: put this somewhere else
    private static final String RIDE_KEY = "filled ride";
    private Ride ride;
    private String driverNum;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            findRide();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_form);
        //TODO ride may not always be invoked by constructor
        bindNewRideVM(null);

        validator = new DriverSignupValidator(binding);

        fab = binding.fab;
        fab.setOnClickListener(v -> {
            if(validator.validate())
            {
                createDriver();
                finish();
            }

        });

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(rideVM.onMapReady());

        setupPlacesAutocomplete();
    }

    private void createDriver()
    {
        Ride ride = new Ride("Test", "4444444444", "", "563b11135e926d03001ac15c", null, null, new ArrayList<String>());
        // TODO: change this to use rideVM.ride.
        RideProvider.getInstance().createRide(ride)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(current -> {
                    Logger.e("Output is", current.toString());
                }, throwable -> {Logger.e("Error:", throwable.getMessage());
                });
    }

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(rideVM.onPlaceSelected());
    }

    private void findRide()
    {
        ride = null;
        ArrayList<String> rideInfo = getIntent().getExtras().getStringArrayList(RIDE_KEY);
        if (rideInfo != null) {
            driverNum = rideInfo.get(0);
            eventID = rideInfo.get(1);
            Logger.d("driver number: " + driverNum + " eventID: " + eventID);

            ArrayList<Ride> rideList = new ArrayList<Ride>();
            RideProvider.getInstance().requestRides()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(results -> findRide(results));

        }
    }

    //finds the specific ride by driver's phone number and event id in the list
    private void findRide(ArrayList<Ride> rideList) {
        for (Ride r : rideList) {
            if (r.driverNumber.equals(driverNum) && r.eventId.equals(eventID)) {
                //ride = r;
                Logger.d("found a match with " + r.driverName);
                bindNewRideVM(r);
                return;
            }
        }
    }

    //binds a ride to the view
    private void bindNewRideVM(Ride r) {
        if (r == null) {
            rideVM = new RideVM(getFragmentManager(), new Ride());
        }
        else {
            rideVM = new RideVM(getFragmentManager(), r, true);
        }
        binding.setRideVM(rideVM);
    }
}
