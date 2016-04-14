package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;

import timber.log.Timber;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.parceler.Parcels;

import java.util.ArrayList;

public class JoinMinistryTeamActivity extends FormActivity
{
    public MinistryTeam ministryTeam;
    public static final String MINISTRY_TEAM = "MINISTRY_TEAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || !bundle.containsKey(AppConstants.MINISTRY_TEAM_KEY))
        {
            Timber.e("JoinMinistryTeamActivity requires that you pass an ministry team ID.");
            Timber.e("Finishing activity...");
            finish();
            return;
        }
        else
        {
            ministryTeam = Parcels.unwrap(bundle.getParcelable(AppConstants.MINISTRY_TEAM_KEY));
        }

        ArrayList<FormContentFragment> fragments = new ArrayList<>();

        MinistryTeamInformationFragment ministryTeamInformationFragment = new MinistryTeamInformationFragment();
        ministryTeamInformationFragment.setArguments(bundle);

        // set up the form holder arraylist with the various fragments
        fragments.add(ministryTeamInformationFragment);
        fragments.add(new BasicInfoFragment());
        fragments.add(new MinistryTeamLeaderInformationFragment());

        // adds the ministry team to the form holder so other fragments will have access to it
        addDataObject(MINISTRY_TEAM, ministryTeam);
        setFormContent(fragments);
    }
}
