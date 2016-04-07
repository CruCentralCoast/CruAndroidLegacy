package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.MapFragment;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.DriverSignupVM;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.observers.Observers;

public class DriverSignupActivity extends BaseAppCompatActivity
{
    SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();

    private DriverSignupVM driverSignupVM;
    private BaseValidator validator;

    @Bind(R.id.fab) FloatingActionButton fab;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private MapFragment mapFragment;

    private String eventID; //will be changed to eventstartdate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form);
        //get event from bundle
        Bundle bundle = getIntent().getExtras();
        eventID = bundle.getString(AppConstants.EVENT_ID, "");
        if(bundle == null || eventID.isEmpty())
        {
            Logger.e("DriverSignupActivity requires that you pass an event");
            Logger.e("Finishing activity...");
            finish();
            return;
        }

        ButterKnife.bind(this);

        setupFab();

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        String rideId = bundle.getString(AppConstants.RIDE_KEY, "");

        if (!rideId.isEmpty())
            requestRides(rideId);
        else
            bindNewRideVM(null);
    }

    //fill in fields that only the DriverSignupActivity has access to but DriverSignupVM doesn't
    private Ride completeRide(Ride r)
    {
        r.gcmID = CruApplication.getGCMID();
        r.eventId = eventID;
        return r;
    }

    private void createDriver()
    {
        RideProvider.createRide(this, Observers.empty(), completeRide(driverSignupVM.getRide()));
    }

    private void updateDriver()
    {
        RideProvider.updateRide(this, Observers.empty(), completeRide(driverSignupVM.getRide()));
    }

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint(AppConstants.AUTOCOMPLETE_HINT);
        autocompleteFragment.setOnPlaceSelectedListener(driverSignupVM.createPlaceSelectionListener());
    }

    private void requestRides(String rideId)
    {
        //TODO GeocodeProvider was used to populate the address of the Ride at this time
        RideProvider.requestRideByID(this,
                Observers.create(
                        ride -> {
                            bindNewRideVM(ride);
                        }
                ), rideId);
    }

    private void bindNewRideVM(Ride r) {
        //new ride
        if (r == null)
            driverSignupVM = new DriverSignupVM(this, getFragmentManager(), eventID);
        //editing an existing ride
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
            //if fields are valid, update shared preferences and the Ride
            if(validator.validate())
            {
                sharedPreferences.edit().putString(AppConstants.USER_NAME, driverSignupVM.nameField.getText().toString()).apply();
                sharedPreferences.edit().putString(AppConstants.USER_PHONE_NUMBER, driverSignupVM.phoneField.getText().toString()).apply();

                if(driverSignupVM.editing)
                    updateDriver();
                else
                    createDriver();

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
