package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.threeten.bp.format.DateTimeFormatter;

public class RideResultVM
{
    public Ride ride;

    public RideResultVM(Ride ride)
    {
        this.ride = ride;
    }

    public String getDateTime()
    {
        return ride.time.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
