package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.RideFilter;
import org.androidcru.crucentralcoast.databinding.PassengerFormRideInfoBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideFilterVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

public class RideInfoFragment extends FormContentFragment {

    private PassengerFormRideInfoBinding binding;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private RideInfoValidator validator;

    private RideFilterVM rideFilterVM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = PassengerFormRideInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        rideFilterVM = new RideFilterVM(getActivity().getFragmentManager(), new RideFilter());
        binding.setRideFilterVM(rideFilterVM);

        validator = new RideInfoValidator(binding);

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        setupPlacesAutocomplete();
    }

    @Override
    public void setupUI() {}

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(rideFilterVM.onPlaceSelected());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(autocompleteFragment != null)
        {
            autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            ((PassengerSignupActivity) getActivity()).filter = rideFilterVM.rideFilter;
            super.onNext();
        }
    }
}
