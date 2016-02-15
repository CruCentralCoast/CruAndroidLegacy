package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;

import com.google.android.gms.location.places.Place;

import org.androidcru.crucentralcoast.data.models.Ride;

public class RideFilterVM extends BaseRideVM
{

    public RideFilterVM(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    protected void syncFromDate()
    {
        //TODO
    }

    @Override
    protected void syncFromTime()
    {
        //TODO
    }

    @Override
    protected void syncToDate()
    {
        //TODO
    }

    @Override
    protected void syncToTime()
    {
        //TODO
    }

    @Override
    protected void placeSelected(Place place)
    {
        //TODO
    }

    @Override
    protected void tripTypeSelected(Ride.Direction direction)
    {
        //TODO
    }

    @Override
    protected void genderSelected(String gender)
    {
        //TODO
    }
}
