package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.view.View;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup.PassengerSignupActivity;
import org.threeten.bp.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class CruEventVM extends BaseObservable
{
    public CruEvent cruEvent;
    public final ObservableBoolean isExpanded = new ObservableBoolean();

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    private Activity parent;

    public CruEventVM(CruEvent cruEvent, boolean isExpanded, Activity activity)
    {
        this.cruEvent = cruEvent;
        this.isExpanded.set(isExpanded);
        this.parent = activity;
    }

    public String getDateTime()
    {
        return cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
                + " - " + cruEvent.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
    }

    public View.OnClickListener onPassengerClicked()
    {
        Intent intent = new Intent(parent, PassengerSignupActivity.class);
        intent.putExtra(PassengerSignupActivity.EVENT_ID, cruEvent.mId);
        return v -> parent.startActivity(intent);
    }

    public View.OnClickListener onDriverClicked()
    {
        return v -> parent.startActivity(new Intent(parent, DriverSignupActivity.class));
    }
}
