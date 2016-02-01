package org.androidcru.crucentralcoast.presentation.views.fragments.driversignup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by main on 1/31/2016.
 */
public class DriverRideInfoFragment extends Fragment implements Validator.ValidationListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener         {

    @Select(defaultSelection = -1) @Bind(R.id.car_capacity_field) Spinner carCapacityField;
    @Select(defaultSelection = -1) @Bind(R.id.trip_type_field) Spinner tripTypeField;
    @Bind(R.id.depart_time_field) EditText departTimeField;
    @Bind(R.id.depart_date_field) EditText departDateField;
    @Bind(R.id.return_time_field) EditText returnTimeField;
    @Bind(R.id.return_date_field) EditText returnDateField;
    @Bind(R.id.depart_layout) RelativeLayout departLayout;
    @Bind(R.id.return_layout) RelativeLayout returnLayout;

    private Validator validator;
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
        validator.put(departTimeField, validTimeRule);
        validator.put(returnTimeField, validTimeRule);
        validator.put(departDateField, validDateRule);
        validator.put(returnDateField, validDateRule);

        ArrayAdapter<String> carCapacityAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7"});
        carCapacityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        carCapacityField.setAdapter(carCapacityAdapter);

        ArrayAdapter<String> tripTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"Round Trip", "Departure", "Return"});
        tripTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        tripTypeField.setAdapter(tripTypeAdapter);

        //submitButton.setOnClickListener(v -> validator.validate());
        //submitButton.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600_48dp, android.R.color.white));

        validator.setValidationListener(this);

        /*determine which types of trips are visible*/
        tripTypeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Departure")) {
                    departLayout.setVisibility(View.VISIBLE);
                    returnLayout.setVisibility(View.GONE);
                } else if (selectedItem.equals("Return")) {
                    departLayout.setVisibility(View.GONE);
                    returnLayout.setVisibility(View.VISIBLE);
                } else {
                    departLayout.setVisibility(View.VISIBLE);
                    returnLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*set up time field listeners*/
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

    public void validate()
    {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded()
    {
        Logger.d("Successfully validated driver info");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = LocalDateTime.of(year, Month.of(monthOfYear + 1), dayOfMonth, 0, 0).format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        if (whichType) {
            departDateField.setText(date);
        } else {
            returnDateField.setText(date);
        }
    }

    /*validates the date*/
    private boolean valiDATE(String inputDate)
    {
        /*check date is at least x days before the event*/
        /*check date is at least y days after event*/
        return true;
    }

    private QuickRule<EditText> validDateRule = new QuickRule<EditText>() {
        @Override
        public boolean isValid(EditText editText) {
            String inputDate = editText.getText().toString();
            return (editText.getVisibility() == View.VISIBLE) ? !inputDate.isEmpty() && valiDATE(inputDate) : true;
        }

        @Override
        public String getMessage(Context context) {
            return "Date is not within proper range"; //what is proper range?
        }
    };

    //pretty sure these are not initialized correctly
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
