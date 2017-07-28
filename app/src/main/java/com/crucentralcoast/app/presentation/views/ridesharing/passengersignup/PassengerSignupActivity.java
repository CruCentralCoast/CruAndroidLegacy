package com.crucentralcoast.app.presentation.views.ridesharing.passengersignup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.presentation.views.forms.FormActivity;
import com.crucentralcoast.app.presentation.views.forms.FormContentFragment;

import org.parceler.Parcels;

import java.util.ArrayList;

import timber.log.Timber;

public class PassengerSignupActivity extends FormActivity {
    private CruEvent event;

    public static final String CRU_EVENT = "CRU_EVENT";
    public static final String SELECTED_RIDE = "SELECTED_RIDE";
    public static final String QUERY = "QUERY";
    public static final String SELECTED_TIME = "SELECTED_TIME";
    public static final String LATLNG = "LATLNG";
    public static final String DIRECTION = "DIRECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey(AppConstants.EVENT_KEY)) {
            Timber.e("PassengerSignupActivity requires that you pass an event");
            Timber.e("Finishing activity...");
            finish();
            return;
        }
        else {
            event = Parcels.unwrap(bundle.getParcelable(AppConstants.EVENT_KEY));
            setSubtitle(event.name);
        }

        ArrayList<FormContentFragment> fragments = setupForm(bundle);

        addDataObject(CRU_EVENT, event);
        setFormContent(fragments);

    }

    @NonNull
    private ArrayList<FormContentFragment> setupForm(Bundle bundle) {
        ArrayList<FormContentFragment> fragments = new ArrayList<>();

        fragments.add(new RideInfoFragment());

        DriverResultsFragment resultsFragment = new DriverResultsFragment();
        resultsFragment.setArguments(bundle);
        fragments.add(resultsFragment);
        fragments.add(new BasicInfoFragment());
        return fragments;
    }

    @Override
    public void complete() {
        setResult(Activity.RESULT_OK);
        super.complete();
    }
}
