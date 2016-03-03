package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.MapFragment;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.providers.GeocodeProvider;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.DriverSignupVM;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class DriverSignupActivity extends AppCompatActivity
{
    private DriverSignupVM driverSignupVM;
    private BaseValidator validator;

    @Bind(R.id.fab) FloatingActionButton fab;
    PlaceAutocompleteFragment autocompleteFragment;
    MapFragment mapFragment;

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || bundle.getString(AppConstants.EVENT_ID, "").isEmpty())
        {
            Logger.e("PassengerSignupActivity requires that you pass an event ID.");
            Logger.e("Finishing activity...");
            finish();
            return;
        }
        else
        {
            eventID = bundle.getString(AppConstants.EVENT_ID, "");
        }
        ButterKnife.bind(this);

        setupFab();

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        String rideId = getIntent().getExtras().getString(AppConstants.RIDE_KEY);

        if (rideId != null)
            requestRides(rideId);
        else
            bindNewRideVM(null);
    }

    private Ride completeRide(Ride r)
    {
        r.gcmID = CruApplication.getGCMID();
        r.eventId = eventID;
        return r;
    }

    private void createDriver()
    {
        RideProvider.createRide(completeRide(driverSignupVM.getRide()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(current -> {
                    Logger.d("Output is", current.toString());
                }, throwable -> {
                    Logger.e("Error:", throwable.getMessage());
                });
    }

    private void updateDriver()
    {
        RideProvider.updateRide(completeRide(driverSignupVM.getRide()))
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
        autocompleteFragment.setOnPlaceSelectedListener(driverSignupVM.onPlaceSelected());
    }

    private void requestRides(String rideId)
    {
        RideProvider.requestRideByID(rideId)
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

    private void bindNewRideVM(Ride r) {
        if (r == null)
            driverSignupVM = new DriverSignupVM(this, getFragmentManager());
        else
            driverSignupVM = new DriverSignupVM(this, getFragmentManager(), r);
        mapFragment.getMapAsync(driverSignupVM.onMapReady());
        setupPlacesAutocomplete();
        validator = new BaseValidator(driverSignupVM);
    }

    private void setupFab()
    {
        fab.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_check_grey600_48dp, android.R.color.white));
        fab.setOnClickListener(v -> {
            if(validator.validate())
            {
                if(driverSignupVM.editing)
                {
                    updateDriver();
                }
                else
                    createDriver();
                finish();
            }

        });
    }
}
