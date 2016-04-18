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
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.observers.Observers;

public class BasicInfoFragment extends FormContentFragment {
    SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();

    private Ride ride;
    private BaseValidator validator;
    private Ride.Direction direction;

    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @Bind(R.id.progress) ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.passenger_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        validator = new BaseValidator(this);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        formHolder.setTitle(getString(R.string.passenger_contact_info));
        ride = (Ride) formHolder.getDataObject(PassengerSignupActivity.SELECTED_RIDE);
        direction = (Ride.Direction) formHolder.getDataObject(PassengerSignupActivity.DIRECTION);

        nameField.setText(sharedPreferences.getString(AppConstants.USER_NAME, null));
        phoneField.setText(sharedPreferences.getString(AppConstants.USER_PHONE_NUMBER, null));
    }

    private Passenger getPassenger()
    {
        return new Passenger(nameField.getText().toString(), phoneField.getText().toString(), CruApplication.getGCMID(), direction);
    }

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            // gets the validated information and overwrites the user's information in shared preferences on a background thread
            sharedPreferences.edit().putString(AppConstants.USER_NAME, nameField.getText().toString()).apply();
            sharedPreferences.edit().putString(AppConstants.USER_PHONE_NUMBER, phoneField.getText().toString()).apply();

            Passenger passenger = getPassenger();
            progressBar.setVisibility(View.VISIBLE);
            formHolder.setNavigationClickable(false);

            PassengerProvider.addPassenger(this, Observers.create(passenger1 -> RideProvider.addPassengerToRide(this, Observers.empty(), ride.id, passenger1.id)), passenger);

            super.onNext();
        }

    }
}

