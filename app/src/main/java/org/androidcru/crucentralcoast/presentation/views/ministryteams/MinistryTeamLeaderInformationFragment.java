package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MinistryTeamLeaderInformationFragment extends FormContentFragment
{
    private MinistryTeam ministryTeam;

    @Bind(R.id.ministry_leader_info_text_view) TextView ministryTeamLeaderInfo;

    public MinistryTeamLeaderInformationFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ministry_team_leader_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // gets back the ministry team object from the form holder.
        ministryTeam = (MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM);

        formHolder.setTitle(ministryTeam.name);

        // For each ministry team leader insert their information into the text view.
        // This should be done with injecting custom views
        for (CruUser user : ministryTeam.ministryTeamLeaders)
        {
            ministryTeamLeaderInfo.setText(
                    ministryTeamLeaderInfo.getText().toString() +
                            user.name.firstName + " " + user.name.lastName + "\n    " +
                            (user.email != null ? user.email + "\n    " : "")  +
                            (user.phone != null ? user.phone + "\n" : "") + "\n");
        }
    }
}
