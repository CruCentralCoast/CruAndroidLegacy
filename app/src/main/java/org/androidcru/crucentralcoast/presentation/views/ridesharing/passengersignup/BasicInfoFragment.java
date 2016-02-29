package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

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
import rx.android.schedulers.AndroidSchedulers;

public class BasicInfoFragment extends FormContentFragment {

    private Ride ride;
    private BaseValidator validator;

    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @Bind(R.id.progress_bar) ProgressBar progressBar;

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
    }

    private Passenger getPassenger()
    {
        Passenger passenger = new Passenger();
        passenger.name = nameField.getText().toString();
        passenger.phone = phoneField.getText().toString();
        return passenger;
    }

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            Passenger passenger = getPassenger();
            progressBar.setVisibility(View.VISIBLE);
            formHolder.setNavigationClickable(false);
            passenger.direction = ride.direction;
            passenger.gcm_id = CruApplication.getGCMID();
            PassengerProvider.getInstance().addPassenger(passenger)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(passenger1 -> {
                    RideProvider.getInstance().addPassengerToRide(passenger1.id, ride.id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(v -> {}, e -> {}, () -> super.onNext());
                });
        }

    }

    @Override
    public void setupUI()
    {
        ride = (Ride) formHolder.getDataObject();
    }
}

