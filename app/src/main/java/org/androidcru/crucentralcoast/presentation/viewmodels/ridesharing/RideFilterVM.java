package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.orhanobut.logger.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Calendar;

import rx.Observable;

public class RideFilterVM
{
    private static final int NUM_HOURS = 24;
    private static final int NUM_MINUTES = 60;
    private static final int INTERVAL = 15;
    private static final int NUM_TIMES = NUM_HOURS * (NUM_MINUTES / INTERVAL);
    private static Observable<Integer> hours = Observable.range(0, NUM_HOURS);
    private static Observable<Integer> minutes = Observable.range(0, NUM_MINUTES).filter(m -> m % INTERVAL == 0);
    private static Timepoint[] timepoints = hours.flatMap((h) -> minutes.map((m) -> new Timepoint(h, m)))
            .toList().toBlocking().first().toArray(new Timepoint[NUM_TIMES]);

    public FragmentManager fm;
    public ObservableField<Ride.Direction> direction;

    public RideFilterVM(FragmentManager fm)
    {
        this.fm = fm;
        direction = new ObservableField<>(null);
    }

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

    public View.OnClickListener onToEventDateClicked()
    {
        return v -> {
            DatePickerDialog dpd = getDateDialog();
            dpd.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                ((EditText) v).setText(LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth).format(DateTimeFormatter.ISO_LOCAL_DATE));
                //TODO sync data
            });
            dpd.show(fm, "whatever");
        };
    }

    public View.OnClickListener onToEventTimeClicked()
    {

        return v -> {
            TimePickerDialog tpd = getTimeDialog();
            tpd.setOnTimeSetListener((view, hourOfDay, minute, second) -> {
                ((EditText) v).setText(LocalTime.of(hourOfDay, minute, second).format(DateTimeFormatter.ISO_LOCAL_TIME));
                //TODO sync data
            });
            tpd.show(fm, "whatever");
        };


    }

    public View.OnClickListener onFromEventDateClicked()
    {
        return v -> {
            DatePickerDialog dpd = getDateDialog();
            dpd.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                ((EditText) v).setText(LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth).format(DateTimeFormatter.ISO_LOCAL_DATE));
                //TODO sync data
            });
            dpd.show(fm, "whatever");
        };
    }

    public View.OnClickListener onFromEventTimeClicked()
    {
        return v -> {
            TimePickerDialog tpd = getTimeDialog();
            tpd.setOnTimeSetListener((view, hourOfDay, minute, second) -> {
                ((EditText) v).setText(LocalTime.of(hourOfDay, minute, second).format(DateTimeFormatter.ISO_LOCAL_TIME));
                //TODO sync data
            });
            tpd.show(fm, "whatever");
        };
    }


    public AdapterView.OnItemSelectedListener onGenderSelected()
    {
        return new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0)
                {
                    //ride.gender = ((TextView) view).getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
    }

    public AdapterView.OnItemSelectedListener onTripTypeSelected()
    {
        return new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //direction.set(position > 0 ? Ride.Direction.values()[position - 1] : null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
    }

    public PlaceSelectionListener onPlaceSelected()
    {
        return new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //TODO data sync
            }

            @Override
            public void onError(Status status) {
                Logger.i("ERROR:", "An error occurred: " + status);
            }
        };
    }
}
