package com.crucentralcoast.app.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.presentation.views.conttactcards.UserContactCardsAdapter;
import com.crucentralcoast.app.presentation.views.forms.FormContentListFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinistryTeamLeaderInformationFragment extends FormContentListFragment
{
    private MinistryTeam ministryTeam;

    @BindView(R.id.informational_text) TextView informationText;


    public MinistryTeamLeaderInformationFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_leader_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        helper.swipeRefreshLayout.setEnabled(false);
        informationText.setText("No Leaders Assigned to this Ministry Team!");
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        // gets back the ministry team object from the form holder.
        ministryTeam = (MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM);

        formHolder.setTitle(ministryTeam.name);

        if (ministryTeam.ministryTeamLeaders != null && !ministryTeam.ministryTeamLeaders.isEmpty())
        {
            helper.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            helper.recyclerView.setAdapter(new UserContactCardsAdapter(ministryTeam.ministryTeamLeaders));
        }
        else
        {
            helper.onEmpty(R.layout.empty_with_alert);
        }
    }
}
