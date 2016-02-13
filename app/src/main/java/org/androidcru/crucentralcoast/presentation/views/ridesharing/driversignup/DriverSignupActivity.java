package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideVM;

public class DriverSignupActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private ActivityDriverFormBinding binding;
    private RideVM rideVM;
    private DriverSignupValidator validator;

    private FloatingActionButton fab;

    private static final double MILE_METER_CONV = 1609.34;
    private static final double CALPOLY_LAT = 35.30021;
    private static final double CALPOLY_LNG = -120.66310;
    private int mapSetter;  /*used for setting markers in map appropriately*/
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Circle mapCircle;
    private Marker departMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_form);
        rideVM = new RideVM(getFragmentManager(), new Ride());
        binding.setRideVM(rideVM);

        validator = new DriverSignupValidator(binding);

        fab = binding.fab;
        fab.setOnClickListener(v -> {
            if(validator.validate())
                finish();
        });

        autocompleteFragment = new SupportPlaceAutocompleteFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.place_autocomplete_frag, autocompleteFragment).commit();
        mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        setupPlacesAutocomplete();
    }

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Logger.i("Location ", "Place: " + place.getName());
                /*remove other departure markers on the map*/
                if (departMarker != null) {
                    departMarker.remove();
                }
                /*add new marker and circle to the map*/
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14.0f));
                departMarker = map.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title("Target Location"));
                mapCircle = map.addCircle(new CircleOptions()
                        .center(place.getLatLng()));
                        //.radius(getMapRadius(radiusField.getText().toString())));
            }

            @Override
            public void onError(Status status) {
                Logger.i("ERROR:", "An error occurred: " + status);
            }
        });
    }

    private double getMapRadius(String input)
    {
        /*default is 1 mile radius*/
        double radius = MILE_METER_CONV;;
        try
        {
            radius = Double.parseDouble(input.toString());
            radius *= MILE_METER_CONV;
        }
        catch (NumberFormatException e)
        {
            radius = MILE_METER_CONV;
            Logger.i(input + " was not a number");
        }
        return radius;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(autocompleteFragment != null)
        {
            autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (map == null)
        {
            map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CALPOLY_LAT, CALPOLY_LNG), 14.0f));
            mapFragment.getView().setClickable(false);
        }
        else
        {
            Logger.d("Unable to display map....");
        }
    }
}
