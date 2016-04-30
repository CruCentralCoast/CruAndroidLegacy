package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.MapFragment;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.DriverSignupEditingVM;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.DriverSignupVM;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.observers.Observers;
import timber.log.Timber;

public class DriverSignupActivity extends BaseAppCompatActivity
{
    SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();

    private DriverSignupVM driverSignupVM;
    private BaseValidator validator;

    @BindView(R.id.fab) FloatingActionButton fab;
    private SupportPlaceAutocompleteFragment autocompleteFragment;

    @BindView(com.google.android.gms.R.id.place_autocomplete_search_input) EditText searchInput;
    private MapFragment mapFragment;

    private CruEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form);
        //get event from bundle
        Bundle bundle = getIntent().getExtras();
        event = (CruEvent)Parcels.unwrap(bundle.getParcelable(AppConstants.EVENT_KEY));
        if(bundle == null || event == null)
        {
            Timber.e("DriverSignupActivity requires that you pass an event");
            Timber.e("Finishing activity...");
            finish();
            return;
        }

        unbinder = ButterKnife.bind(this);

        setupFab();

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        String rideId = bundle.getString(AppConstants.RIDE_KEY, "");

        if (!rideId.isEmpty())
            requestRides(rideId);
        else
            bindNewRideVM(null);
    }

    @OnClick(R.id.autocomplete_layout)
    public void onAutocompleteTouched(View v)
    {
        if(getCurrentFocus() != null)
            getCurrentFocus().clearFocus();
        searchInput.callOnClick();
    }

    //fill in fields that only the DriverSignupActivity has access to but DriverSignupVM doesn't
    private Ride completeRide(Ride r)
    {
        r.gcmID = CruApplication.getGCMID();
        r.eventId = event.id;
        return r;
    }

    private void createDriver()
    {
        RideProvider.createRide(Observers.empty(), completeRide(driverSignupVM.getRide()));
    }

    private void updateDriver()
    {
        RideProvider.updateRide(Observers.empty(), completeRide(driverSignupVM.getRide()));
    }

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint_driver));
        autocompleteFragment.setOnPlaceSelectedListener(driverSignupVM.createPlaceSelectionListener());
    }

    private void requestRides(String rideId)
    {
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
            driverSignupVM = new DriverSignupVM(this, getFragmentManager(), event.id, event.startDate, event.endDate);
        //editing an existing ride
        else
            driverSignupVM = new DriverSignupEditingVM(this, getFragmentManager(), r, event.endDate);
        mapFragment.getMapAsync(driverSignupVM.onMapReady());
        setupPlacesAutocomplete();
        validator = new BaseValidator(driverSignupVM);
    }

    private void setupFab()
    {
        fab.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_check_grey600, android.R.color.white));
        fab.setOnClickListener(v -> {
            //if fields are valid, update shared preferences and the Ride
            if(validator.validate())
            {
                sharedPreferences.edit().putString(AppConstants.USER_NAME, driverSignupVM.nameField.getText().toString()).apply();
                sharedPreferences.edit().putString(AppConstants.USER_PHONE_NUMBER, driverSignupVM.phoneField.getText().toString()).apply();

                if(driverSignupVM instanceof DriverSignupEditingVM)
                    updateDriver();
                else
                    createDriver();

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
