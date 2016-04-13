package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.viewmodels.BaseVM;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;
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

    private DatePickerDialog getDateDialog(GregorianCalendar c)
    {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                null,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dpd.vibrate(false);
        dpd.setMaxDate(eventEndDate);
        return dpd;
    }

    private TimePickerDialog getTimeDialog(GregorianCalendar c)
    {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                null,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        tpd.vibrate(false);
        tpd.setSelectableTimes(timepoints);
        return tpd;
    }

    protected void onEventDateClicked(View v, GregorianCalendar gc)
    {
        DatePickerDialog dpd;
        //use Ride's start time if editing a Ride
        if (rideSetDate == null)
            dpd = getDateDialog(gc);
        else
            dpd = getDateDialog(new GregorianCalendar(rideSetDate.getYear(), rideSetDate.getMonthValue() - 1, rideSetDate.getDayOfMonth()));
        //sets the text of the DatePicker EditText
        dpd.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
            rideSetDate = LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth);
            String yyyymmdd = rideSetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            ((EditText) v).setText(convertToddMMyyyy(yyyymmdd));
        });

        dpd.show(fm, AppConstants.SUPER_SPECIAL_STRING);
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
            String milTime = rideSetTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
            ((EditText) v).setText(convertTo12Hour(milTime));
        });
        tpd.show(fm, AppConstants.SUPER_SPECIAL_STRING);
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
                Timber.i("ERROR:", "An error occurred: " + status);
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
