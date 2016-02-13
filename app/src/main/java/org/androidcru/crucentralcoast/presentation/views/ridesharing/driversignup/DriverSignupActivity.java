package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideVM;

public class DriverSignupActivity extends AppCompatActivity
{
    private ActivityDriverFormBinding binding;
    private RideVM rideVM;

    //lol don't ask. SO is God. http://stackoverflow.com/a/124179/1822968
    public static final String PHONE_REGEX = "1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?" +
            "\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\se?x?t?(\\d*))?";

    @NotEmpty private EditText name;
    @NotEmpty @Pattern(regex = PHONE_REGEX) private EditText phoneNumber;

    @Select private Spinner gender;
    @Select private Spinner carCapacity;
    @Select private Spinner tripType;

    private EditText toEventDate;
    private EditText toEventTime;

    private EditText fromEventDate;
    private EditText fromEventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_form);
        rideVM = new RideVM(new Ride());
        binding.setRideVM(rideVM);

        bindUI();
        setupDateTimeFields();
        setupSpinners();
    }

    private void bindUI()
    {
        name = binding.nameField;
        phoneNumber = binding.phoneField;
        gender = binding.genderField;
        carCapacity = binding.carCapacityField;
        tripType = binding.tripTypeField;
        toEventDate = binding.toEventDateField;
        toEventTime = binding.toEventTimeField;
        fromEventDate = binding.fromEventDateField;
        fromEventTime = binding.fromEventTimeField;
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
    }


}
