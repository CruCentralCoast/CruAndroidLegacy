package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.RideFilter;
import org.androidcru.crucentralcoast.presentation.BindingAdapters;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import butterknife.Bind;
import butterknife.OnClick;

public class RideFilterVM extends BaseRideVM
{
    private RideFilter rideFilter;

    @Bind(R.id.trip_type_field) Spinner tripTypeField;
    @Bind(R.id.gender_field) Spinner genderField;

    private Ride.Direction[] directions;
    private String[] genders;

    public RideFilterVM(View rootView, FragmentManager fm, RideFilter rideFilter)
    {
        super(rootView, fm);
        this.rideFilter = rideFilter;
        generateDirections();
        bindUI();
    }

    private void generateDirections()
    {
        directions = Ride.Direction.values();
    }

    @Override
    protected void placeSelected(LatLng precisePlace, String placeAddress)
    {
        rideFilter.location = precisePlace;
    }

    public RideFilter getRideFilter()
    {
        rideFilter.direction = retrieveDirection(tripTypeField, directions);
        rideFilter.gender = (String) genderField.getSelectedItem();
        rideFilter.dateTime = ZonedDateTime.of(date, time, ZoneId.systemDefault());
        return rideFilter;
    }

    private void bindUI()
    {
        BindingAdapters.setSpinner(tripTypeField, directionsForSpinner(directions), null, 0);
        BindingAdapters.setSpinner(genderField, gendersForSpinner(R.array.genders), null, 0);

    }

    @OnClick(R.id.event_time_field)
    public void onTimeClicked(View v)
    {
        onEventTimeClicked(v);
    }

    @OnClick(R.id.event_date_field)
    public void onDateClicked(View v)
    {
        onEventDateClicked(v);
    }


}
