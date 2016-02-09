package org.androidcru.crucentralcoast.presentation.viewmodels.ministryteams;

import android.view.View;

import org.androidcru.crucentralcoast.data.models.MinistryTeam;

public class MinistryTeamVM
{
    public MinistryTeam ministryTeam;

    public MinistryTeamVM(MinistryTeam ministryTeam)
    {
        this.ministryTeam = ministryTeam;
    }

    public View.OnClickListener onTeamClick()
    {
        return v -> {
            if (ministryTeam.mCruImage != null)
            {
                // TODO send them to the view pager with the appropriate ministry team selected
            }
        };
    }
}
