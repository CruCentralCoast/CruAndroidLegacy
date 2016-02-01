package org.androidcru.crucentralcoast.presentation.views.fragments.driversignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class DriverRideLocFragment extends Fragment implements OnMapReadyCallback {

    private static final double MILE_METER_CONV = 1609.34;
    private static final double CALPOLY_LAT = 35.30021;
    private static final double CALPOLY_LNG = -120.66310;

    @Bind(R.id.radius_field) EditText radiusField;

    private int mapSetter;  /*used for setting markers in map appropriately*/
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Circle mapCircle;
    private Marker departMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /*autocomplete*/
        autocompleteFragment = new SupportPlaceAutocompleteFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.place_autocomplete_frag, autocompleteFragment).commit();
        /*map*/
        mapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        return inflater.inflate(R.layout.driver_form_ride_loc, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        /*autocomplete search*/
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Logger.i("Depart", "Place: " + place.getName());
                Logger.d("Depart + Place: " + place.getName());
                /*remove other departure markers on the map*/
                if (departMarker != null) {
                    departMarker.remove();
                }
                /*add new marker and circle to the map*/
                departMarker = map.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title("Target Location"));
                mapCircle = map.addCircle(new CircleOptions()
                        .center(place.getLatLng())
                        .radius(getMapRadius(radiusField.getText().toString())));
            }

            @Override
            public void onError(Status status) {
                Logger.i("ERROR:", "An error occurred: " + status);
            }
        });

        /*radius watcher*/
        radiusField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mapCircle != null) {
                    LatLng temp = mapCircle.getCenter();
                    mapCircle.remove();
                    mapCircle = map.addCircle(new CircleOptions()
                            .center(temp)
                            .radius(getMapRadius(s.toString())));
                }
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
        Logger.i("using number " + radius);
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
    public void onMapReady(GoogleMap googleMap) {
        /*default*/
        if (map == null)
        {
            map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CALPOLY_LAT, CALPOLY_LNG), 14.0f));
        }
        else
        {
            /*hmmm what to do here*/
            Logger.d("Unable to display map....");
        }
    }
}
