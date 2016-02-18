package org.androidcru.crucentralcoast.presentation.viewmodels.ministryteams;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.views.ministryteams.JoinMinistryTeamActivity;

public class MinistryTeamVM
{
    private Activity parent;
    public MinistryTeam ministryTeam;

    public MinistryTeamVM(Activity parent, MinistryTeam ministryTeam)
    {
        this.parent = parent;
        this.ministryTeam = ministryTeam;
    }

    public View.OnClickListener onTeamClick()
    {
        return v -> {
            if (ministryTeam.cruImage != null)
            {
                Intent intent = new Intent(parent, JoinMinistryTeamActivity.class);
                intent.putExtra("MINISTRY_TEAM", CruApplication.gson.toJson(ministryTeam));
            }
        };
    }
}
