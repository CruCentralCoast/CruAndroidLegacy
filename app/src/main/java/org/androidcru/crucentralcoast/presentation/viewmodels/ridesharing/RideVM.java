package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.androidcru.crucentralcoast.R;
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

@SuppressWarnings("unused")
public class RideVM extends BaseObservable
{
    public Ride ride;
    public ObservableField<Ride.Direction> direction;

    private static final int NUM_HOURS = 24;
    private static final int NUM_MINUTES = 60;
    private static final int INTERVAL = 15;
    private static final int NUM_TIMES = NUM_HOURS * (NUM_MINUTES / INTERVAL);

    private static Observable<Integer> hours = Observable.range(0, NUM_HOURS);
    private static Observable<Integer> minutes = Observable.range(0, NUM_MINUTES).filter(m -> m % INTERVAL == 0);
    private static Timepoint[] timepoints = hours.flatMap((h) -> minutes.map((m) -> new Timepoint(h, m)))
            .toList().toBlocking().first().toArray(new Timepoint[NUM_TIMES]);

    public RideVM(Ride ride)
    {
        this.ride = ride;
        direction = new ObservableField<>(null);
    }

    public void onDriverNameChanged(CharSequence s, int start, int before, int count)
    {
        ride.driverName = s.toString();
    }

    public void onDriverNumberChanged(CharSequence s, int start, int before, int count)
    {
        ride.driverNumber = s.toString();
    }
    
    public void setupGenderSpinner(Spinner spinner)
    {
        Context context = spinner.getContext();
        String[] genders = context.getResources().getStringArray(R.array.genders);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.simple_spinner_item,
                genders);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0)
                {
                    ride.gender = genders[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void setupCarCapacitySpinner(Spinner spinner)
    {
        Context context = spinner.getContext();
        String[] carCapacity = context.getResources().getStringArray(R.array.car_capacity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.simple_spinner_item,
                carCapacity);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0)
                {
                    ride.carCapacity = Integer.valueOf(carCapacity[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void setupTripTypeSpinner(Spinner spinner)
    {
        Context context = spinner.getContext();
        String[] tripType = context.getResources().getStringArray(R.array.trip_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.simple_spinner_item,
                tripType);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                direction.set(position > 0 ? Ride.Direction.values()[position - 1] : null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private TimePickerDialog getTimeDialog(TextView textView)
    {
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Calendar c = DateTimeUtils.toGregorianCalendar(now);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                (view, hourOfDay, minute, second) -> {
                    textView.setText(LocalTime.of(hourOfDay, minute, second).format(DateTimeFormatter.ISO_LOCAL_TIME));
                    //TODO sync data
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        tpd.vibrate(false);
        tpd.setSelectableTimes(timepoints);
        return tpd;
    }

    private DatePickerDialog getDateDialog(TextView textView)
    {
        ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Calendar c = DateTimeUtils.toGregorianCalendar(now);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    textView.setText(LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth).format(DateTimeFormatter.ISO_LOCAL_DATE));
                    //TODO sync data
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dpd.vibrate(false);
        return dpd;
    }

    public void setupTimeDialog(FragmentManager fragmentManager, EditText editText)
    {
        editText.setOnClickListener(v -> getTimeDialog(editText).show(fragmentManager, "whatever"));
    }

    public void setupDateDialog(FragmentManager fragmentManager, EditText editText)
    {
        editText.setOnClickListener(v -> getDateDialog(editText).show(fragmentManager, "whatever"));
    }
}
