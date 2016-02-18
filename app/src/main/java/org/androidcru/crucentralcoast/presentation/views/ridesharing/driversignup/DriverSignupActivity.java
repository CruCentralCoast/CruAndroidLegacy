package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.MapFragment;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;
import org.androidcru.crucentralcoast.presentation.providers.GeocodeProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideVM;

import rx.android.schedulers.AndroidSchedulers;

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
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_layout);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || bundle.getString(CruApplication.EVENT_ID, "").isEmpty())
        {
            Logger.e("PassengerSignupActivity requires that you pass an event ID.");
            Logger.e("Finishing activity...");
            finish();
            return;
        }
        else
        {
            eventID = bundle.getString(CruApplication.EVENT_ID, "");
        }

        if (getIntent().getExtras().containsKey(RIDE_KEY)) {
            requestRides();
        }
        else
            bindNewRideVM(null);

        //TODO ride may not always be invoked by constructor
    }

    private void createDriver()
    {
//        Ride ride = new Ride("Test", "4444444444", "Man", "563b11135e926d03001ac15c", ZonedDateTime.now(),
//                new Location("93405", "CA", "San Luis Obispo", "1 Grand Ave", "USA"), new ArrayList<>(), 1.0, Ride.Direction.TO,
//                CruApplication.getGCMID(), 4);
        // TODO: change this to use rideVM.ride.
        //Logger.d(rideVM.ride.eventId);
        RideProvider.getInstance().createRide(rideVM.ride)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(current -> {
                    Logger.d("Output is", current.toString());
                }, throwable -> {
                    Logger.e("Error:", throwable.getMessage());
                });
    }

    private void updateDriver()
    {
        RideProvider.getInstance().updateRide(rideVM.ride)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(current -> {
                    Logger.d("Output is", current.toString());
                }, throwable -> {
                    Logger.e("Error:", throwable.getMessage());
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

    private void requestRides()
    {
        String rideId = getIntent().getExtras().getString(RIDE_KEY);
        RideProvider.getInstance().requestRideByID(rideId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ride -> {
                    GeocodeProvider.getLatLng(this, ride.location.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(address ->
                        {
                            ride.address = address;
                            bindNewRideVM(ride);
                        });

                });
    }

    //binds a ride to the view
    private void bindNewRideVM(Ride r) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_form);
        validator = new DriverSignupValidator(binding);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        setupFab();
        if (r == null) {
            rideVM = new RideVM(getFragmentManager(), new Ride());
        }
        else {
            rideVM = new RideVM(getFragmentManager(), r, true);
        }
        rideVM.ride.eventId = eventID;
        rideVM.ride.gcmID = CruApplication.getGCMID();
        mapFragment.getMapAsync(rideVM.onMapReady());
        setupPlacesAutocomplete();

        binding.setRideVM(rideVM);
        /*if (r != null) {
            fillForm(r);
        }*/
    }

    private void setupFab()
    {
        fab = binding.fab;
        fab.setOnClickListener(v -> {
            if(validator.validate())
            {
                if(rideVM.editing)
                {
                    updateDriver();
                }
                else
                    createDriver();
                finish();
            }

        });
    }

    /*private void fillForm(Ride r) {
        Logger.d("car capacity is " + r.carCapacity);
        binding.carCapacityField.setSelection(r.carCapacity + 1, true);
        //TODO: find better way to do this
        binding.genderField.setSelection(r.gender.equals("Male") ? 1 : 2);

        binding.radiusField.setText("" + r.radius);

//        GeocodeProvider.getLatLng(getApplicationContext(), r.location.toString())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(result -> rideVM.setMap(result));
    }*/
}
