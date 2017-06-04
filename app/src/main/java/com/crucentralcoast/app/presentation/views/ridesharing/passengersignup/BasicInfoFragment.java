package com.crucentralcoast.app.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.PassengerProvider;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.presentation.validator.BaseValidator;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.presentation.views.forms.FormContentFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.util.AutoFill;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.observers.Observers;
import timber.log.Timber;

public class BasicInfoFragment extends FormContentFragment {
    private Ride ride;
    private BaseValidator validator;
    private Ride.Direction direction;

    @BindView(R.id.name_field)
    @NotEmpty
    EditText nameField;
    @BindView(R.id.phone_field)
    @NotEmpty
    @Pattern(regex = AppConstants.PHONE_REGEX)
    EditText phoneField;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.passenger_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        validator = new BaseValidator(this);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private Passenger getPassenger() {
        return new Passenger(nameField.getText().toString(),
                phoneField.getText().toString(), SharedPreferencesUtil.getFCMID(), direction, ride.eventId);
    }

    @Override
    public void onNext(FormHolder formHolder) {
        if (validator.validate()) {
            SharedPreferencesUtil.writeBasicInfo(nameField.getText().toString(), null, phoneField.getText().toString());

            Passenger passenger = getPassenger();
            progressBar.setVisibility(View.VISIBLE);
            formHolder.setNavigationClickable(false);

            PassengerProvider.addPassenger(this, Observers.create(passenger1 ->
                    RideProvider.addPassengerToRide(this, Observers.create(x ->
                            super.onNext(formHolder), Timber::e), ride.id, passenger1.id), Timber::e), passenger);
        }
    }

    @Override
    public void setupData(FormHolder formHolder) {
        formHolder.setTitle(getString(R.string.passenger_contact_info));
        ride = (Ride) formHolder.getDataObject(PassengerSignupActivity.SELECTED_RIDE);
        direction = (Ride.Direction) formHolder.getDataObject(PassengerSignupActivity.DIRECTION);
        AutoFill.setupAutoFillData((BaseAppCompatActivity) getActivity(), () -> {
            nameField.setText(SharedPreferencesUtil.getUserName());
            phoneField.setText(SharedPreferencesUtil.getUserPhoneNumber());
        });
    }
}

