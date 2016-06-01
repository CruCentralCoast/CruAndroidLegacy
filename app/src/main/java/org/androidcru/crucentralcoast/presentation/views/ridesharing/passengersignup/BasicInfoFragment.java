package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.PassengerProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.androidcru.crucentralcoast.util.AutoFill;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.observers.Observers;

public class BasicInfoFragment extends FormContentFragment
{
    private Ride ride;
    private BaseValidator validator;
    private Ride.Direction direction;

    @BindView(R.id.name_field) @NotEmpty EditText nameField;
    @BindView(R.id.phone_field) @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @BindView(R.id.progress) ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.passenger_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        validator = new BaseValidator(this);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private Passenger getPassenger()
    {
        return new Passenger(nameField.getText().toString(), phoneField.getText().toString(), SharedPreferencesUtil.getGCMID(), direction);
    }

    @Override
    public void onNext(FormHolder formHolder)
    {
        if(validator.validate())
        {
            SharedPreferencesUtil.writeBasicInfo(nameField.getText().toString(), null, phoneField.getText().toString());

            Passenger passenger = getPassenger();
            progressBar.setVisibility(View.VISIBLE);
            formHolder.setNavigationClickable(false);

            PassengerProvider.addPassenger(this, Observers.create(passenger1 -> RideProvider.addPassengerToRide(this, Observers.empty(), ride.id, passenger1.id)), passenger);

            super.onNext(formHolder);
        }

    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        formHolder.setTitle(getString(R.string.passenger_contact_info));
        ride = (Ride) formHolder.getDataObject(PassengerSignupActivity.SELECTED_RIDE);
        direction = (Ride.Direction) formHolder.getDataObject(PassengerSignupActivity.DIRECTION);
        AutoFill.setupAutoFillData((BaseAppCompatActivity)getActivity(), () -> {
            nameField.setText(SharedPreferencesUtil.getUserName());
            phoneField.setText(SharedPreferencesUtil.getUserPhoneNumber());
        });
    }
}

