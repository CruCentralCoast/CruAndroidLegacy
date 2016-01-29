package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.orhanobut.logger.Logger;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import com.google.android.gms.common.api.GoogleApiClient;


public class DriverFragment extends Fragment implements Validator.ValidationListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
{
    //lol don't ask. SO is God. http://stackoverflow.com/a/124179/1822968
    public static final String PHONE_REGEX = "1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?" +
            "\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\se?x?t?(\\d*))?";

    @NotEmpty @Bind(R.id.name_field) EditText mNameField;
    @NotEmpty @Pattern(regex = PHONE_REGEX) @Bind(R.id.phone_field) EditText mPhoneField;
    @Select(defaultSelection = -1) @Bind(R.id.sex_field) Spinner mSexField;
    @Select(defaultSelection = -1) @Bind(R.id.car_capacity_field) Spinner mCarCapacityField;
    @Select(defaultSelection = -1) @Bind(R.id.trip_type_field) Spinner mTripTypeField;
    @Bind(R.id.depart_time_field) EditText mDepartTimeField;
    @Bind(R.id.depart_date_field) EditText mDepartDateField;
    @Bind(R.id.return_time_field) EditText mReturnTimeField;
    @Bind(R.id.return_date_field) EditText mReturnDateField;
    @Bind(R.id.depart_layout) RelativeLayout mDepartLayout;
    @Bind(R.id.return_layout) RelativeLayout mReturnLayout;
    @Bind(R.id.submit_button) FloatingActionButton mSubmitButton;

    private Validator mValidator;
    private boolean mDepart; /*used for setting time in appropriate field*/
    public final static String DATE_FORMATTER = "M/d/y";

    private SupportPlaceAutocompleteFragment mDepartAutocompleteFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mDepartAutocompleteFragment = new SupportPlaceAutocompleteFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.depart_place_autocomplete_layout, mDepartAutocompleteFragment).commit();
        return inflater.inflate(R.layout.driver_form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mValidator = new Validator(this);
        mValidator.put(mDepartTimeField, validTimeRule);
        mValidator.put(mReturnTimeField, validTimeRule);
        mValidator.put(mDepartDateField, validDateRule);
        mValidator.put(mReturnDateField, validDateRule);

        mPhoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"Male", "Female"});
        sexAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSexField.setAdapter(sexAdapter);

        ArrayAdapter<String> carCapacityAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7"});
        carCapacityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mCarCapacityField.setAdapter(carCapacityAdapter);

        ArrayAdapter<String> tripTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"Round Trip", "Departure", "Return"});
        tripTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mTripTypeField.setAdapter(tripTypeAdapter);

        mSubmitButton.setOnClickListener(v -> mValidator.validate());
        mSubmitButton.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600_48dp, android.R.color.white));

        mValidator.setValidationListener(this);

        /*determine which types of trips are visible*/
        mTripTypeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Departure")) {
                    mDepartLayout.setVisibility(View.VISIBLE);
                    mReturnLayout.setVisibility(View.GONE);
                }
                else if(selectedItem.equals("Return")) {
                    mDepartLayout.setVisibility(View.GONE);
                    mReturnLayout.setVisibility(View.VISIBLE);
                }
                else {
                    mDepartLayout.setVisibility(View.VISIBLE);
                    mReturnLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /*set up time field listeners*/
        mDepartTimeField.setKeyListener(null);
        mDepartTimeField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    DriverFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            mDepart = true;
        });

        mDepartDateField.setKeyListener(null);
        mDepartDateField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    DriverFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            mDepart = true;
        });

        mReturnTimeField.setKeyListener(null);
        mReturnTimeField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    DriverFragment.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            mDepart = false;
        });

        mReturnDateField.setKeyListener(null);
        mReturnDateField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    DriverFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            mDepart = false;
        });

        /*autocomplete map*/

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        mDepartAutocompleteFragment.setFilter(typeFilter);
        mDepartAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                Logger.i("Depart", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status)
            {
                Logger.i("ERROR:", "An error occurred: " + status);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(mDepartAutocompleteFragment != null)
        {
            mDepartAutocompleteFragment.onActivityResult(requestCode, resultCode, data);
        }
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
        if (mDepart) {
            mDepartTimeField.setText(time);
        } else {
            mReturnTimeField.setText(time);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = LocalDateTime.of(year, Month.of(monthOfYear + 1), dayOfMonth, 0, 0).format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        if (mDepart) {
            mDepartDateField.setText(date);
        } else {
            mReturnDateField.setText(date);
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
