package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.PassengerProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.databinding.PassengerFormBasicInfoBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.PassengerVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import rx.android.schedulers.AndroidSchedulers;

public class BasicInfoFragment extends FormContentFragment {

    private PassengerFormBasicInfoBinding binding;
    private PassengerVM passengerVM;
    private Ride ride;

    private BasicInfoValidator validator;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = PassengerFormBasicInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passengerVM = new PassengerVM(new Passenger());
        binding.setPassengerVM(passengerVM);

        validator = new BasicInfoValidator(binding);

        progressBar = binding.progressBar;
    }

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            progressBar.setVisibility(View.VISIBLE);
            formHolder.setNavigationClickable(false);
            passengerVM.passenger.direction = ride.direction;
            passengerVM.passenger.gcm_id = CruApplication.getGCMID();
            PassengerProvider.getInstance().addPassenger(passengerVM.passenger)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(passenger -> {
                    RideProvider.getInstance().addPassengerToRide(passenger.id, ride.id)
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

