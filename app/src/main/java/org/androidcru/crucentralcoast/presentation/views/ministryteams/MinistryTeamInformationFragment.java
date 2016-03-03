package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MinistryTeamInformationFragment extends FormContentFragment
{
    private MinistryTeam ministryTeam;

    @Bind(R.id.ministry_info_image) ImageView ministryInfoImage;
    @Bind(R.id.ministry_team_description) TextView ministryTeamDescription;

    public MinistryTeamInformationFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ministry_team_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ministryTeam = (MinistryTeam) formHolder.getDataObject();

        //TODO temporary for now
        ViewUtil.setSource(ministryInfoImage, ministryTeam.cruImage.url, 0, null, null);
        ministryTeamDescription.setText(ministryTeam.description);

    }

    @Override
    public void setupUI()
    {
        formHolder.setTitle(ministryTeam.name);
    }
}
