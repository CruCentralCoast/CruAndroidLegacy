package org.androidcru.crucentralcoast.presentation.views.fragments.driversignup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.orhanobut.logger.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.fragments.ProvableFragment;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DriverRideInfoFragment extends ProvableFragment implements Validator.ValidationListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener         {

    @Select(defaultSelection = -1) @Bind(R.id.car_capacity_field) Spinner carCapacityField;
    @Select(defaultSelection = -1) @Bind(R.id.trip_type_field) Spinner tripTypeField;
    @Bind(R.id.depart_time_field) EditText departTimeField;
    @Bind(R.id.depart_date_field) EditText departDateField;
    @Bind(R.id.return_time_field) EditText returnTimeField;
    @Bind(R.id.return_date_field) EditText returnDateField;
    @Bind(R.id.depart_layout) RelativeLayout departLayout;
    @Bind(R.id.return_layout) RelativeLayout returnLayout;

    private Validator validator;
    private boolean isValid;
    private Calendar departDate;
    private Calendar returnDate;
    private String selectedType;
    private boolean whichType; /*used for setting time in appropriate field*/
    public final static String DATE_FORMATTER = "M/d/y";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.driver_form_ride_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        validator = new Validator(this);
        /*add custom rules*/
        validator.put(departTimeField, validTimeRule);
        validator.put(returnTimeField, validTimeRule);
        validator.put(departDateField, validDateRule);
        validator.put(returnDateField, validDateRule);

        /*car capacity field*/
        ArrayAdapter<String> carCapacityAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7"});
        carCapacityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        carCapacityField.setAdapter(carCapacityAdapter);

        /*trip type field*/
        ArrayAdapter<String> tripTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"Round Trip", "Departure", "Return"});
        tripTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        tripTypeField.setAdapter(tripTypeAdapter);

        validator.setValidationListener(this);

        /*determine which types of trips are visible*/
        tripTypeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Departure")) {
                    departLayout.setVisibility(View.VISIBLE);
                    returnLayout.setVisibility(View.GONE);
                    selectedType = "departure";
                } else if (selectedItem.equals("Return")) {
                    departLayout.setVisibility(View.GONE);
                    returnLayout.setVisibility(View.VISIBLE);
                    selectedType = "return";
                } else {
                    departLayout.setVisibility(View.VISIBLE);
                    returnLayout.setVisibility(View.VISIBLE);
                    selectedType = "roundtrip";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*set up date and time field listeners*/
        departTimeField.setKeyListener(null);
        departTimeField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    DriverRideInfoFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.vibrate(false);
            tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            whichType = true;
        });

        departDateField.setKeyListener(null);
        departDateField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            departDate = now;
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    DriverRideInfoFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.vibrate(false);
            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            whichType = true;
        });

        returnTimeField.setKeyListener(null);
        returnTimeField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    DriverRideInfoFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.vibrate(false);
            tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            whichType = false;
        });

        returnDateField.setKeyListener(null);
        returnDateField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            returnDate = now;
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    DriverRideInfoFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.vibrate(false);
            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            whichType = false;
        });
    }

    @Override
    public boolean validate()
    {
        validator.validate();
        return isValid;
    }

    @Override
    public void onValidationSucceeded()
    {
        Logger.d("Successfully validated driver info");
        isValid = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        isValid = false;
        for (ValidationError e : errors)
        {
            View v = e.getView();
            if (v instanceof Spinner)
            {
                ((TextView) ((Spinner) v).getSelectedView()).setError(e.getCollatedErrorMessage(getContext()));
            }
            else
            {
                ((EditText)v).setError(e.getCollatedErrorMessage(getContext()));
            }

        }
    }

    /*sets up format for displaying the time after it has been set*/
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String ampm = hourOfDay < 12 ? "AM" : "PM";
        String time = "" + (hourOfDay % 12) + ":" + minuteString + " " + ampm;
        if (whichType) {
            departTimeField.setText(time);
        } else {
            returnTimeField.setText(time);
        }
    }

    /*sets up format for displaying date after it has been set*/
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = LocalDateTime.of(year, Month.of(monthOfYear + 1), dayOfMonth, 0, 0).format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        if (whichType) {
            departDateField.setText(date);
        } else {
            returnDateField.setText(date);
        }
    }

    /*does exactly what the name implies*/
    private boolean valiDATE(String inputDate)
    {
        /*if trip type isn't round trip, make sure depart date is before return date*/
        return !selectedType.equals("roundtrip") || returnDate.before(departDate);
    }

    /*validator rule for date*/
    private QuickRule<EditText> validDateRule = new QuickRule<EditText>() {
        @Override
        public boolean isValid(EditText editText) {
            String inputDate = editText.getText().toString();
            return (editText.getVisibility() == View.VISIBLE) ? !inputDate.isEmpty() && valiDATE(inputDate) : true;
        }

        @Override
        public String getMessage(Context context) {
            return "Date not set correctly";
        }
    };

    /*validtor rule for time*/
    private QuickRule<EditText> validTimeRule = new QuickRule<EditText>() {
        @Override
        public boolean isValid(EditText timeEditText) {
            return (timeEditText.getVisibility() == View.VISIBLE) ? !timeEditText.getText().toString().isEmpty() : true;
        }

        @Override
        public String getMessage(Context context) {
            return "Time not set";
        }
    };
}
