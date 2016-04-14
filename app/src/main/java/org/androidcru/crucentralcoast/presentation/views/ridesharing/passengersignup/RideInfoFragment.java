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
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideFilterVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

public class RideInfoFragment extends FormContentFragment {

    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private BaseValidator validator;

    private RideFilterVM rideFilterVM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.passenger_form_ride_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        formHolder.setTitle(getString(R.string.passenger_signup));
        rideFilterVM = new RideFilterVM(this, getActivity().getFragmentManager(), (CruEvent) formHolder.getDataObject(PassengerSignupActivity.CRU_EVENT));
        validator = new BaseValidator(rideFilterVM);
        setupPlacesAutocomplete();
    }

    private void setupPlacesAutocomplete()
    {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint_passenger));
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(rideFilterVM.createPlaceSelectionListener());
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
            formHolder.addDataObject(PassengerSignupActivity.QUERY, rideFilterVM.getQuery());
            formHolder.addDataObject(PassengerSignupActivity.LATLNG, rideFilterVM.precisePlace);
            super.onNext();
        }
    }
}
