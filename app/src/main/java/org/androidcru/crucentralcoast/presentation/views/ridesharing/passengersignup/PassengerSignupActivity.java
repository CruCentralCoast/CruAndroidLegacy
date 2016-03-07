package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.parceler.Parcels;

import java.util.ArrayList;

public class PassengerSignupActivity extends FormActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || !bundle.containsKey(AppConstants.EVENT_KEY))
        {
            Logger.e("PassengerSignupActivity requires that you pass an event");
            Logger.e("Finishing activity...");
            finish();
            return;
        }
        else
        {
            CruEvent event = Parcels.unwrap(bundle.getParcelable(AppConstants.EVENT_KEY));
            setSubtitle(event.name);
        }

        ArrayList<FormContentFragment> fragments = setupForm(bundle);

        setAdapter(new PassengerPagerAdapter(getSupportFragmentManager(), this, fragments));


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
