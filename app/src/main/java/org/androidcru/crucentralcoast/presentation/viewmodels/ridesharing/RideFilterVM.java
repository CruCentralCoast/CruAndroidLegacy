package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.RideFilter;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

public class RideFilterVM extends BaseRideVM
{

    public RideFilter rideFilter;
    private LocalDate toDate;
    private LocalDate fromDate;
    private LocalTime toTime;
    private LocalTime fromTime;

    public RideFilterVM(FragmentManager fm, RideFilter rideFilter)
    {
        super(fm);
        this.rideFilter = rideFilter;
    }

    private void updateFromDateTime()
    {
        if(fromDate != null && fromTime != null)
        {
            rideFilter.fromDateTime = ZonedDateTime.of(fromDate, fromTime, ZoneId.systemDefault());
        }
    }

    private void updateToDateTime()
    {
        if(toDate != null && toTime != null)
        {
            rideFilter.toDateTime = ZonedDateTime.of(toDate, toTime, ZoneId.systemDefault());
        }
    }

    @Override
    protected void syncFromDate(LocalDate date)
    {
        fromDate = date;
        updateFromDateTime();
    }

    @Override
    protected void syncFromTime(LocalTime time)
    {
        fromTime = time;
        updateFromDateTime();
    }

    @Override
    protected void syncToDate(LocalDate date)
    {
        toDate = date;
        updateToDateTime();
    }

    @Override
    protected void syncToTime(LocalTime time)
    {
        toTime = time;
        updateToDateTime();
    }

    @Override
    protected void placeSelected(LatLng precisePlace, String placeAdress)
    {
        rideFilter.location = precisePlace;
    }

    @Override
    protected void tripTypeSelected(Ride.Direction direction)
    {
        rideFilter.direction = direction;
    }

    @Override
    protected void genderSelected(String gender)
    {
        rideFilter.gender = gender;
    }
}
