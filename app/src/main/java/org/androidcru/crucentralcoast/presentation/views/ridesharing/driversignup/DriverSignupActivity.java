package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideVM;

import java.util.List;

public class DriverSignupActivity extends AppCompatActivity implements Validator.ValidationListener
{
    private ActivityDriverFormBinding binding;
    private RideVM rideVM;
    private Validator validator;

    private FloatingActionButton fab;

    //lol don't ask. SO is God. http://stackoverflow.com/a/124179/1822968
    public static final String PHONE_REGEX = "1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?" +
            "\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\se?x?t?(\\d*))?";

    @NotEmpty private EditText name;
    @NotEmpty @Pattern(regex = PHONE_REGEX) private EditText phoneNumber;

    @Select private Spinner gender;
    @Select private Spinner carCapacity;
    @Select private Spinner tripType;

    @NotEmpty private EditText toEventDate;
    @NotEmpty private EditText toEventTime;

    @NotEmpty private EditText fromEventDate;
    @NotEmpty private EditText fromEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_form);
        rideVM = new RideVM(new Ride());
        binding.setRideVM(rideVM);

        setupValidator();

        bindUI();
        setupDateTimeFields();
        setupSpinners();
        setupFAB();
    }

    private void setupValidator()
    {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void bindUI()
    {
        fab = binding.fab;

        name = binding.nameField;
        phoneNumber = binding.phoneField;
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        gender = binding.genderField;
        carCapacity = binding.carCapacityField;
        tripType = binding.tripTypeField;
        toEventDate = binding.toEventDateField;
        toEventTime = binding.toEventTimeField;
        fromEventDate = binding.fromEventDateField;
        fromEventTime = binding.fromEventTimeField;
    }

    private void setupFAB()
    {
        fab.setOnClickListener(v -> validator.validate());
    }

    private void setupDateTimeFields()
    {
        FragmentManager fm = getFragmentManager();
        toEventDate.setKeyListener(null);
        rideVM.setupDateDialog(fm, toEventDate);

        toEventTime.setKeyListener(null);
        rideVM.setupTimeDialog(fm, toEventTime);

        fromEventDate.setKeyListener(null);
        rideVM.setupDateDialog(fm, fromEventDate);

        fromEventTime.setKeyListener(null);
        rideVM.setupTimeDialog(fm, fromEventTime);
    }

    private void setupSpinners()
    {
        if(gender != null)
            rideVM.setupGenderSpinner(gender);
        if(carCapacity != null)
            rideVM.setupCarCapacitySpinner(carCapacity);
        if(tripType != null)
            rideVM.setupTripTypeSpinner(tripType);
    }


    @Override
    public void onValidationSucceeded()
    {
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        for (ValidationError e : errors)
        {
            View v = e.getView();
            if (v instanceof Spinner)
            {
                ((TextView) ((Spinner) v).getSelectedView()).setError(e.getCollatedErrorMessage(this));
            }
            else if (v instanceof EditText)
            {
                if(v.getParent() instanceof TextInputLayout)
                {
                    TextInputLayout parent = (TextInputLayout) v.getParent();
                    parent.setError(e.getFailedRules().get(0).getMessage(this));
                }
                else
                {
                    ((EditText) v).setError(e.getFailedRules().get(0).getMessage(this));
                }
            }
        }
    }
}
