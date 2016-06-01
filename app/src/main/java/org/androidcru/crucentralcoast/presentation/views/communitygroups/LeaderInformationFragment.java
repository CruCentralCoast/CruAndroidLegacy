package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderInformationFragment extends FormContentFragment
{
        private CommunityGroup communityGroup;

        @BindView(R.id.leader_info_text_view) TextView leaderInfo;

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

            // For each ministry team leader insert their information into the text view.
            // This should be done with injecting custom views
            for (CruUser user : communityGroup.leaders)
            {
                leaderInfo.setText(leaderInfo.getText().toString() + user.name.firstName + " " + user.name.lastName + "\n");
            }
        }
}
