package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.viewmodels.BaseVM;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Calendar;

import rx.Observable;

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

    protected LocalDate date;
    protected LocalTime time;
    private String[] genders;

    protected FragmentManager fm;

    public BaseRideVM(Activity activity, FragmentManager fm)
    {
        super(activity);
        this.fm = fm;
    }

    public BaseRideVM(Fragment fragment, FragmentManager fm)
    {
        super(fragment);
        this.fm = fm;
    }

    public BaseRideVM(View rootView, FragmentManager fm)
    {
        super(rootView);
        this.fm = fm;
    }

    protected abstract void placeSelected(LatLng precisePlace, String placeAddress);


    private TimePickerDialog getTimeDialog()
    {
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Calendar c = DateTimeUtils.toGregorianCalendar(now);
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

    private DatePickerDialog getDateDialog()
    {
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Calendar c = DateTimeUtils.toGregorianCalendar(now);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                null,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dpd.vibrate(false);
        return dpd;
    }

    protected void onEventDateClicked(View v)
    {
        DatePickerDialog dpd = getDateDialog();
        dpd.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
            date = LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth);
            ((EditText) v).setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        });
        dpd.show(fm, "whatever");
    }

    protected void onEventTimeClicked(View v)
    {
        TimePickerDialog tpd = getTimeDialog();
        tpd.setOnTimeSetListener((view, hourOfDay, minute, second) -> {
            time = LocalTime.of(hourOfDay, minute, second);
            ((EditText) v).setText(time.format(DateTimeFormatter.ISO_LOCAL_TIME));
        });
        tpd.show(fm, "whatever");
    }


    public PlaceSelectionListener onPlaceSelected()
    {
        return new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placeSelected(place.getLatLng(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                Logger.i("ERROR:", "An error occurred: " + status);
            }
        };
    }

    protected String[] directionsForSpinner(Ride.Direction[] directions)
    {
        String[] directionsForSpinner = new String[directions.length + 1];
        directionsForSpinner[0] = "Select Direction";
        for(int i = 0; i < directions.length; i++)
        {
            directionsForSpinner[i] = directions[i].getValueDetailed();
        }
        return directionsForSpinner;
    }

    protected int getDirectionIndex(Ride.Direction d, Ride.Direction[] directions)
    {
        int index = 0;

        if(d == null)
            return index;

        for(int i = 0; i < directions.length; i++)
        {
            if(d == directions[i])
            {
                index = i + 1;
                break;
            }
        }
        return index;
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

    protected String[] gendersForSpinner(String[] actualGenders)
    {
        genders = new String[actualGenders.length + 1];
        genders[0] = "Select Genders";
        System.arraycopy(actualGenders, 0, genders, 1, actualGenders.length);
        return genders;
    }

    protected String[] gendersForSpinner(int resourceId)
    {
        String[] actualGenders = context.getResources().getStringArray(resourceId);
        genders = new String[actualGenders.length + 1];
        genders[0] = "Select Genders";
        System.arraycopy(actualGenders, 0, genders, 1, actualGenders.length);
        return genders;
    }
}
