package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.providers.ApiProvider;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.BindingAdapters;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MinistryTeamLeaderInformationFragment extends FormContentFragment
{
    private static CruApiService cruService = ApiProvider.getInstance().getService();
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

        ministryTeam = (MinistryTeam) formHolder.getDataObject();
        ArrayList<String> ministryIdList = new ArrayList<>();
        ministryIdList.add(ministryTeam.mId);
        // ask jon what the toBlocking single stuff does
        ArrayList<CruUser> ministryTeamLeaders = cruService.getMinistryTeamLeaders(ministryIdList).subscribeOn(Schedulers.io()).toBlocking().single();

        //TODO make this pretty


        for (CruUser user : ministryTeamLeaders)
        {
            ministryTeamLeaderInfo.setText(
                    ministryTeamLeaderInfo.getText().toString() +
                    user.mName.mFirstName + " " + user.mName.mLastName + "\n    " +
                    user.mEmail + "\n    " +
                    user.phoneNumber + "\n\n");
        }
    }

    @Override
    public void setupUI()
    {
        formHolder.setTitle(ministryTeam.name);
        formHolder.setPreviousVisibility(View.GONE);
        formHolder.setNextText("FINISH");
    }
}
