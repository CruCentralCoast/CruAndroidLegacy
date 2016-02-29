package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup.PassengerSignupActivity;
import org.threeten.bp.format.DateTimeFormatter;

public class CruEventVM
{
    public CruEvent cruEvent;
    public boolean isExpanded;

    private Activity parent;

    public CruEventVM(CruEvent cruEvent, boolean isExpanded, Activity activity)
    {
        this.cruEvent = cruEvent;
        this.isExpanded = isExpanded;
        this.parent = activity;
    }

    public String getDateTime()
    {
        return cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER))
                + " - " + cruEvent.mEndDate.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER));
    }

    public View.OnClickListener onPassengerClicked()
    {
        Intent intent = new Intent(parent, PassengerSignupActivity.class);
        intent.putExtra(AppConstants.EVENT_ID, cruEvent.mId);
        return v -> parent.startActivity(intent);
    }

    public View.OnClickListener onDriverClicked()
    {
        Intent intent = new Intent(parent, DriverSignupActivity.class);
        intent.putExtra(AppConstants.EVENT_ID, cruEvent.mId);
        return v -> parent.startActivity(intent);
    }
}
