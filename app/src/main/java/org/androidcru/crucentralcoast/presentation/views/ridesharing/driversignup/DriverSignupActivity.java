package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.MapFragment;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideVM;

public class DriverSignupActivity extends AppCompatActivity
{
    private ActivityDriverFormBinding binding;
    private RideVM rideVM;
    private DriverSignupValidator validator;

    private FloatingActionButton fab;

    private PlaceAutocompleteFragment autocompleteFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_form);
        //TODO ride may not always be invoked by constructor
        rideVM = new RideVM(getFragmentManager(), new Ride());
        binding.setRideVM(rideVM);

        validator = new DriverSignupValidator(binding);

        fab = binding.fab;
        fab.setOnClickListener(v -> {
            if(validator.validate())
                finish();
        });

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(rideVM.onMapReady());

        setupPlacesAutocomplete();
    }

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(rideVM.onPlaceSelected());
    }
}
