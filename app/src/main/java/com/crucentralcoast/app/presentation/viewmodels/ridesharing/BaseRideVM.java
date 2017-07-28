package com.crucentralcoast.app.presentation.viewmodels.ridesharing;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.presentation.viewmodels.BaseVM;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import rx.Observable;
import timber.log.Timber;

@SuppressWarnings("unused")
public abstract class BaseRideVM extends BaseVM
{
    protected static final int NUM_HOURS = 24;
    protected static final int NUM_MINUTES = 60;
    protected static final int INTERVAL = 15;
    protected static final int NUM_TIMES = NUM_HOURS * (NUM_MINUTES / INTERVAL);
    protected static Observable<Integer> hours = Observable.range(0, NUM_HOURS);
    protected static Observable<Integer> minutes = Observable.range(0, NUM_MINUTES).filter(m -> m % INTERVAL == 0);
    protected static Timepoint[] timepoints = hours.flatMap((h) -> minutes.map((m) -> new Timepoint(h, m)))
            .toList().toBlocking().first().toArray(new Timepoint[NUM_TIMES]);

    protected LocalDate rideSetDate;
    protected LocalTime rideSetTime;
    protected GregorianCalendar eventEndDate;
    private String[] genders;

    private FragmentManager fm;

    public BaseRideVM(BaseAppCompatActivity activity, FragmentManager fm)
    {
        super(activity);
        this.fm = fm;
    }

    public BaseRideVM(BaseSupportFragment fragment, FragmentManager fm)
    {
        super(fragment);
        this.fm = fm;
    }

    protected abstract void placeSelected(LatLng precisePlace, String placeAddress);

    private DatePickerDialog getDateDialog(Context context, GregorianCalendar c,
                                           DatePickerDialog.OnDateSetListener listener)
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, listener,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        Calendar minDate = new GregorianCalendar(c.get(Calendar.YEAR) - 1,
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(eventEndDate.getTimeInMillis());
        return datePickerDialog;
    }

    private TimePickerDialog getTimeDialog(GregorianCalendar c)
    {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                null,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        tpd.setSelectableTimes(timepoints);
        return tpd;
    }

    protected void onEventDateClicked(View v, GregorianCalendar gc)
    {
        DatePickerDialog dpd;
        //sets the text of the DatePicker EditText
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            rideSetDate = LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth);
            String yyyymmdd = rideSetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            ((EditText) v).setText(convertToddMMyyyy(yyyymmdd));
        };
        //use Ride's start time if editing a Ride
        if (rideSetDate == null)
            dpd = getDateDialog(v.getContext(), gc, listener);
        else
            dpd = getDateDialog(v.getContext(),
                    new GregorianCalendar(rideSetDate.getYear(), rideSetDate.getMonthValue() - 1,
                            rideSetDate.getDayOfMonth()), listener);
        dpd.show();
    }

    protected void onEventTimeClicked(View v, GregorianCalendar gc)
    {
        TimePickerDialog tpd;
        if (rideSetTime == null)
            tpd = getTimeDialog(gc);
        else
            tpd = getTimeDialog(new GregorianCalendar(0, 0, 0, rideSetTime.getHour(), rideSetTime.getMinute()));
        //sets the text of the TimePicker EditText
        tpd.setOnTimeSetListener((view, hourOfDay, minute, second) -> {
            rideSetTime = LocalTime.of(hourOfDay, minute, second);
            ((EditText) v).setText(getTimeString(rideSetTime));
        });
        tpd.show(fm, AppConstants.SUPER_SPECIAL_STRING);
    }

    protected String getTimeString(LocalTime time)
    {
        return convertTo12Hour(rideSetTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
    }

    protected String getDateString(LocalDate date)
    {
        return convertToddMMyyyy(rideSetDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    private String convertToddMMyyyy(String s)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_yyyyMMdd);
        Date d = null;

        try
        {
            d = sdf.parse(s);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        sdf.applyPattern(AppConstants.DATE_DISPLAY_PATTERN);
        return sdf.format(d);
    }

    private String convertTo12Hour(String t)
    {
        DateFormat f1 = new SimpleDateFormat(AppConstants.TIME_PARSE);
        DateFormat f2 = new SimpleDateFormat(AppConstants.TIME_FORMAT);
        Date d = null;

        try
        {
            d = f1.parse(t);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return f2.format(d);
    }

    public PlaceSelectionListener createPlaceSelectionListener()
    {
        return new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placeSelected(place.getLatLng(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                Timber.i("ERROR: An error occurred: " + status);
            }
        };
    }

    protected String[] directionsForSpinner(Ride.Direction[] directions)
    {
        String[] directionsForSpinner = new String[directions.length + 1];
        directionsForSpinner[0] = context.getString(R.string.default_direction_op);
        for(int i = 0; i < directions.length; i++)
        {
            directionsForSpinner[i + 1] = directions[i].getValueDetailed();
        }
        return directionsForSpinner;
    }

    protected Ride.Direction retrieveDirection(Spinner tripTypeField, Ride.Direction[] directions)
    {
        return directions[tripTypeField.getSelectedItemPosition() - 1];
    }

    protected int getGenderIndex(String g)
    {
        int index = 0;

        if(g == null)
            return index;

        for(int i = 1; i < genders.length; i++)
        {
            if(g.equals(genders[i]))
            {
                index = i;
                break;
            }
        }
        return index;
    }

    protected String[] gendersForSpinner()
    {
        ArrayList<String> genders = new ArrayList<>(Ride.Gender.getColloquials());
        genders.add(0, context.getString(R.string.default_gender_op));
        this.genders = genders.toArray(new String[genders.size()]);
        return this.genders;
    }
}
