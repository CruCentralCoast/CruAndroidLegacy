package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.parceler.Parcels;

import java.util.ArrayList;

import timber.log.Timber;

public class PassengerSignupActivity extends FormActivity
{
    private CruEvent event;

    public static final String CRU_EVENT = "CRU_EVENT";
    public static final String SELECTED_RIDE = "SELECTED_RIDE";
    public static final String QUERY = "QUERY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || !bundle.containsKey(AppConstants.EVENT_KEY))
        {
            Timber.e("PassengerSignupActivity requires that you pass an event");
            Timber.e("Finishing activity...");
            finish();
            return;
        }
        else
        {
            event = Parcels.unwrap(bundle.getParcelable(AppConstants.EVENT_KEY));
            setSubtitle(event.name);
        }

        ArrayList<FormContentFragment> fragments = setupForm(bundle);

        addDataObject(CRU_EVENT, event);
        setFormContent(fragments);

    }

    @NonNull
    private ArrayList<FormContentFragment> setupForm(Bundle bundle)
    {
        ArrayList<FormContentFragment> fragments = new ArrayList<>();

        fragments.add(new RideInfoFragment());

        DriverResultsFragment resultsFragment = new DriverResultsFragment();
        resultsFragment.setArguments(bundle);
        fragments.add(resultsFragment);
        fragments.add(new BasicInfoFragment());
        return fragments;
    }

    @Override
    public void complete()
    {
        setResult(RESULT_OK);
        super.complete();
    }
}
