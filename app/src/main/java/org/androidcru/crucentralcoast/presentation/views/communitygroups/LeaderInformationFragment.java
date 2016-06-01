package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.presentation.views.conttactcards.UserContactCardsAdapter;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderInformationFragment extends FormContentFragment
{
        private CommunityGroup communityGroup;

    @BindView(R.id.recyclerview) RecyclerView leaderInfo;

        public LeaderInformationFragment()
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
            super.onViewCreated(view, savedInstanceState);
            unbinder = ButterKnife.bind(this, view);
        }

        @Override
        public void setupData(FormHolder formHolder)
        {
            // gets back the ministry team object from the form holder.
            communityGroup = (CommunityGroup) formHolder.getDataObject(AppConstants.COMMUNITY_GROUP);

            formHolder.setTitle(communityGroup.name);

            if (communityGroup.leaders != null)
            {
                leaderInfo.setLayoutManager(new LinearLayoutManager(getContext()));
                leaderInfo.setAdapter(new UserContactCardsAdapter(communityGroup.leaders));
            }
        }
}
