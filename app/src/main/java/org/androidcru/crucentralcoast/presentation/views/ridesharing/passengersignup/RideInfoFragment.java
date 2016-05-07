package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.presentation.customviews.CruSupportPlaceAutocompleteFragment;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideFilterVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

public class RideInfoFragment extends FormContentFragment {

    private CruSupportPlaceAutocompleteFragment autocompleteFragment;
    private BaseValidator validator;

    private RideFilterVM rideFilterVM;

    private String oldPlaceText;
    private Place oldPlace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.passenger_form_ride_info, container, false);
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
    public void onNext(FormHolder formHolder)
    {
        if(validator.validate() &&  autocompleteFragment.validate())
        {
            Pair<Query, Ride.Direction> queryDirectionPair = rideFilterVM.getQuery();
            formHolder.addDataObject(PassengerSignupActivity.QUERY, queryDirectionPair.first);
            formHolder.addDataObject(PassengerSignupActivity.DIRECTION, queryDirectionPair.second);
            formHolder.addDataObject(PassengerSignupActivity.LATLNG, rideFilterVM.precisePlace);

            super.onNext(formHolder);
        }
        else
        {
            //validate also sets error messages, make sure second gets called due to shortciruit
            autocompleteFragment.validate();
        }
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        formHolder.setTitle(getString(R.string.passenger_signup));

        if(rideFilterVM == null)
            rideFilterVM = new RideFilterVM(this, getActivity().getFragmentManager(), (CruEvent) formHolder.getDataObject(PassengerSignupActivity.CRU_EVENT));
        else
            rideFilterVM.bindUI(this);

        validator = new BaseValidator(rideFilterVM);

        autocompleteFragment = (CruSupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        setupPlacesAutocomplete();
    }
}
