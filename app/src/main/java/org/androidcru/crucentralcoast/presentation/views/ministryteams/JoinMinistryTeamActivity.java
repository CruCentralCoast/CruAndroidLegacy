package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.parceler.Parcels;

import java.util.ArrayList;

public class JoinMinistryTeamActivity extends FormActivity
{
    public MinistryTeam ministryTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null || !bundle.containsKey(AppConstants.MINISTRY_TEAM_KEY))
        {
            Logger.e("JoinMinistryTeamActivity requires that you pass an ministry team ID.");
            Logger.e("Finishing activity...");
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

        fragments.add(ministryTeamInformationFragment);
        fragments.add(new BasicInfoFragment());
        fragments.add(new MinistryTeamLeaderInformationFragment());

        setAdapter(new MinistryTeamPagerAdapter(getSupportFragmentManager(), this, fragments));
        setFirstDataObject(ministryTeam);
    }
}
