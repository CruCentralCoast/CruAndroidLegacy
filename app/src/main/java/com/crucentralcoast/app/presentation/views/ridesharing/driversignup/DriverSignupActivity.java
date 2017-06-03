package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.data.providers.UserProvider;
import com.crucentralcoast.app.data.providers.observer.ObserverUtil;
import com.crucentralcoast.app.presentation.customviews.CruSupportPlaceAutocompleteFragment;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.viewmodels.ridesharing.DriverSignupEditingVM;
import com.crucentralcoast.app.presentation.viewmodels.ridesharing.DriverSignupVM;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.MapFragment;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;

public class DriverSignupActivity extends BaseAppCompatActivity {
    private DriverSignupVM driverSignupVM;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private CruSupportPlaceAutocompleteFragment autocompleteFragment;

    @BindView(com.google.android.gms.R.id.place_autocomplete_search_input)
    EditText searchInput;
    private MapFragment mapFragment;

    private CruEvent event;
    private Observer<CruUser> userObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_form);
        //get event from bundle
        Bundle bundle = getIntent().getExtras();
        event = Parcels.unwrap(bundle.getParcelable(AppConstants.EVENT_KEY));
        if (bundle == null || event == null) {
            Timber.e("DriverSignupActivity requires that you pass an event");
            Timber.e("Finishing activity...");
            finish();
            return;
        }

        unbinder = ButterKnife.bind(this);

        setupFab();

        autocompleteFragment = (CruSupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);

        String rideId = bundle.getString(AppConstants.RIDE_KEY, "");

        setupUserObserver();

        if (!rideId.isEmpty())
            requestRides(rideId);
        else
            bindNewRideVM(null);
    }

    @OnClick(R.id.autocomplete_layout)
    public void onAutocompleteTouched(View v) {
        if (getCurrentFocus() != null)
            getCurrentFocus().clearFocus();
        searchInput.callOnClick();
    }

    //fill in fields that only the DriverSignupActivity has access to but DriverSignupVM doesn't
    private Ride completeRide(Ride r) {
        r.fcmId = SharedPreferencesUtil.getFCMID();
        r.eventId = event.id;
        return r;
    }

    private void createDriver() {
        RideProvider.createRide(
                Observers.create(ride -> goToAddPassenger(ride.id), Timber::e),
                completeRide(driverSignupVM.getRide()));
    }

    private void updateDriver() {
        RideProvider.updateRide(Observers.empty(), completeRide(driverSignupVM.getRide()));
    }

    private void setupPlacesAutocomplete() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint_driver));
        autocompleteFragment.setOnPlaceSelectedListener(driverSignupVM.createPlaceSelectionListener());
    }

    private void requestRides(String rideId) {
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
    }

    private void setupFab() {
        fab.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_check_grey600, android.R.color.white));
        fab.setOnClickListener(v -> {
            String number = convString(driverSignupVM.phoneField.getText().toString());
            if (driverSignupVM.validator.validate() && autocompleteFragment.validate()) {
                if (SharedPreferencesUtil.getAuthorizedDriver(number)) {
                    sendRide();
                }
                else {
                    UserProvider.requestCruUser(this, userObserver, number);
                }
            }
        });
    }

    public void goToAddPassenger(String rideId) {
        Intent addPassengerIntent = new Intent(this, AddPassengersActivity.class);
        addPassengerIntent.putExtra(CruEvent.sId, event.id);
        addPassengerIntent.putExtra("rideId", rideId);
        addPassengerIntent.putExtra("available", driverSignupVM.getRide().carCapacity);
        startActivity(addPassengerIntent);
    }

    private void sendRide() {
        SharedPreferencesUtil.writeBasicInfo(driverSignupVM.nameField.getText().toString(), null, driverSignupVM.phoneField.getText().toString());

        if (driverSignupVM instanceof DriverSignupEditingVM) {
            updateDriver();
        }
        else {
            createDriver();
        }

        setResult(RESULT_OK);
        finish();
    }

    //remove anything that isn't a digit
    private String convString(String phone) {
        return phone.replaceAll("\\D", "");
    }

    private void setupUserObserver() {
        userObserver = ObserverUtil.create(Observers.create(t -> {
                    if (t != null) {
                        //update shared preferences with the number
                        SharedPreferencesUtil.setAuthoriziedDriver(convString(driverSignupVM.phoneField.getText().toString()));

                        sendRide();
                    }
                },
                e -> Timber.e(e, "Failed to retrieve User.")),
                this::displayFailure);
    }

    private void displayFailure() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.driver_authorization_title)
                .setMessage(R.string.driver_authorization_msg)
                .setNegativeButton(R.string.driver_authorization_dismiss, (dialog1, which) -> {
                    dialog1.dismiss();
                })
                .create();
        dialog.show();
    }
}
