package com.crucentralcoast.app.presentation.views.ministryteams;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.forms.FormContentFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.presentation.views.settings.SettingsFragment;
import com.crucentralcoast.app.presentation.views.updategroupsinformation.UpdateGroupsInformationActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MinistryTeamInformationFragment extends FormContentFragment
{
    private MinistryTeam ministryTeam;

    @BindView(R.id.ministry_info_image) ImageView ministryInfoImage;
    @BindView(R.id.ministry_team_description) TextView ministryTeamDescription;
    @BindView(R.id.ministry_team_edit_button) Button ministryTeamEditButton;


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
        unbinder = ButterKnife.bind(this, view);

    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        // retrieve the ministry team from the form holder
        ministryTeam = (MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM);
        formHolder.setTitle(ministryTeam.name);
//        SharedPreferencesUtil.writeLoginInformation("59bff21968325a0400be2c2d", "TestName", "TestKey");
        if (isLeader()) {
            ministryTeamEditButton.setVisibility(View.VISIBLE);
        }
        else {
            ministryTeamEditButton.setVisibility(View.INVISIBLE);
        }

        // setup the image that is tied to this ministry team
//        ViewUtil.setSource(ministryInfoImage, (ministryTeam.teamImage != null && !ministryTeam.teamImage.isEmpty()) ? ministryTeam.teamImage : null, 0, null, null);
        ViewUtil.setSource(ministryInfoImage, ministryTeam.teamImage, 0, null, null, null);
        ministryTeamDescription.setText(ministryTeam.description);
    }

    public Boolean isLeader() {
        boolean isLeader = false;
        String loggedInID = SharedPreferencesUtil.getUserId();
        for(CruUser user: ministryTeam.ministryTeamLeaders) {
            if (loggedInID.equals(user.id))
                isLeader = true;
        }
        return isLeader;
    }

    @OnClick(R.id.ministry_team_edit_button)
    public void onClickUpdateMinistryTeamButton() {
        Intent testIntent = new Intent(MinistryTeamInformationFragment.this.getContext(), UpdateGroupsInformationActivity.class);
        testIntent.putExtra("fragmentType", "ministry_team");
        testIntent.putExtra("groupID", ministryTeam.id);
        getActivity().startActivity(testIntent);
    }

}
