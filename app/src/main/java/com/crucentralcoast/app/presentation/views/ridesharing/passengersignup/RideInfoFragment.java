package com.crucentralcoast.app.presentation.views.ridesharing.passengersignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.queries.Query;
import com.crucentralcoast.app.presentation.customviews.CruSupportPlaceAutocompleteFragment;
import com.crucentralcoast.app.presentation.validator.BaseValidator;
import com.crucentralcoast.app.presentation.viewmodels.ridesharing.RideFilterVM;
import com.crucentralcoast.app.presentation.views.forms.FormContentFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;

public class RideInfoFragment extends FormContentFragment {

    private CruSupportPlaceAutocompleteFragment autocompleteFragment;
    private BaseValidator validator;

    private RideFilterVM rideFilterVM;

    private String oldPlaceText;
    private Place oldPlace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.passenger_form_ride_info, container, false);
    }

    private void setupPlacesAutocomplete() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint_passenger));
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(rideFilterVM.createPlaceSelectionListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (autocompleteFragment != null) {
            autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNext(FormHolder formHolder) {
        if (validator.validate() && autocompleteFragment.validate()) {
            Pair<Query, Ride.Direction> queryDirectionPair = rideFilterVM.getQuery();
            formHolder.addDataObject(PassengerSignupActivity.QUERY, queryDirectionPair.first);
            formHolder.addDataObject(PassengerSignupActivity.DIRECTION, queryDirectionPair.second);
            formHolder.addDataObject(PassengerSignupActivity.SELECTED_TIME, rideFilterVM.getDateTime());

            Passenger passenger = new Passenger(SharedPreferencesUtil.getUserName(),
                    SharedPreferencesUtil.getUserPhoneNumber(),
                    SharedPreferencesUtil.getFCMID(), queryDirectionPair.second,
                    ((CruEvent) formHolder.getDataObject(PassengerSignupActivity.CRU_EVENT)).id);
            passenger.location = rideFilterVM.location;
            passenger.genderPref = rideFilterVM.getGender().getId();
            formHolder.addDataObject("passenger", passenger);

            super.onNext(formHolder);
        }
        else {
            // validate also sets error messages, make sure second gets called due to short circuit
            autocompleteFragment.validate();
        }
    }

    @Override
    public void setupData(FormHolder formHolder) {
        formHolder.setTitle(getString(R.string.passenger_signup));

        if (rideFilterVM == null)
            rideFilterVM = new RideFilterVM(this, getActivity().getFragmentManager(), (CruEvent) formHolder.getDataObject(PassengerSignupActivity.CRU_EVENT));
        else
            rideFilterVM.bindUI(this);

        validator = new BaseValidator(rideFilterVM);

        autocompleteFragment = (CruSupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        setupPlacesAutocomplete();
    }
}
