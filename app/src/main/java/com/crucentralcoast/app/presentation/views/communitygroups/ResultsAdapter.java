package com.crucentralcoast.app.presentation.views.communitygroups;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CruName;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.providers.CommunityGroupProvider;
import com.crucentralcoast.app.presentation.viewmodels.ExpandableState;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.presentation.views.ministryteams.MinistryTeamInformationFragment;
import com.crucentralcoast.app.presentation.views.updategroupsinformation.UpdateGroupsInformationActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.observers.Observers;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.CommunityGroupViewHolder>
{
    List<ExpandableState<CommunityGroup>> communityGroups;
    FormHolder formHolder;
    Context context;
    @BindView(R.id.edit_community_group_button1) Button editButton;


    public ResultsAdapter(List<CommunityGroup> communityGroups, FormHolder formHolder)
    {
        this.communityGroups = new ArrayList<>();
        for (CommunityGroup g : communityGroups)
            this.communityGroups.add(new ExpandableState<CommunityGroup>(g));

        this.formHolder = formHolder;
    }

    @Override
    public CommunityGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        return new CommunityGroupViewHolder(inflater.inflate(R.layout.item_community_group_result, parent, false));
    }

    @Override
    public void onBindViewHolder(CommunityGroupViewHolder holder, int position)
    {
        CommunityGroup cg = communityGroups.get(position).model;

        holder.communityGroup = cg;

        holder.title.setText(cg.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) + ", " + cg.meetingTime.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT)));
        holder.name.setText(cg.name);

        holder.leaders.setText("");
        for (int i = 0; i < cg.leaders.size(); i++)
        {

            holder.leaders.setText(holder.leaders.getText().toString() + cg.leaders.get(i).name.firstName +
                    " " + cg.leaders.get(i).name.lastName);

            if (i != cg.leaders.size() - 1)
                holder.leaders.setText(holder.leaders.getText().toString() + ", ");
        }

        holder.description.setText(cg.description);

        holder.details.setVisibility(communityGroups.get(position).isExpanded ? View.VISIBLE : View.GONE);
        holder.editButton.setVisibility(isLeader(cg) ? View.VISIBLE: View.GONE);
    }



    @Override
    public int getItemCount()
    {
        return communityGroups.size();
    }

    public class CommunityGroupViewHolder extends RecyclerView.ViewHolder
    {
        // gets set in BindViewHolder
        public CommunityGroup communityGroup;

        @BindView(R.id.community_group_meeting_time) TextView title;
        @BindView(R.id.community_group_name) TextView name;
        @BindView(R.id.community_group_description) TextView description;
        @BindView(R.id.community_group_leaders) TextView leaders;
        @BindView(R.id.community_group_details) ViewGroup details;
        @BindView(R.id.join_community_group_button) Button joinButton;
        @BindView(R.id.edit_community_group_button1) Button editButton;


        public CommunityGroupViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick (R.id.community_group_result_view)
        public void onTap()
        {
            communityGroups.get(getAdapterPosition()).isExpanded = !communityGroups.get(getAdapterPosition()).isExpanded;

            notifyItemChanged(getAdapterPosition());

            SharedPreferencesUtil.writeLoginInformation("57b5f8ea880dd10300a492", "TestName", "TestKey");


            if (isLeader(communityGroup)) {
                editButton.setVisibility(View.VISIBLE);
            }
            else {
                editButton.setVisibility(View.GONE);
            }

        }

        @OnClick (R.id.join_community_group_button)
        public void onJoin()
        {
            // writes the community group so the correct leaders will be setup.
            formHolder.addDataObject(AppConstants.COMMUNITY_GROUP, communityGroup);

            //send push notification to CG leaders
            //CommunityGroupProvider.joinCommunityGroup(Observers.empty(),));
            CruUser usr = new CruUser(new CruName(SharedPreferencesUtil.getUserName(), ""),
                    SharedPreferencesUtil.getUserEmail(),
                    SharedPreferencesUtil.getUserPhoneNumber());

            CommunityGroupProvider.joinCommunityGroup(Observers.empty(), communityGroup.id, usr);

            formHolder.next();
        }

        @OnClick (R.id.edit_community_group_button1)
        public void onEditCommunityGroup() {
            Intent editMinistryTeamIntent = new Intent(context, UpdateGroupsInformationActivity.class);
            editMinistryTeamIntent.putExtra("fragmentType", "community_group");
            editMinistryTeamIntent.putExtra("groupID", communityGroup.id);
            context.startActivity(editMinistryTeamIntent);
        }
    }

    public Boolean isLeader(CommunityGroup cg) {
        boolean isLeader = false;
        String loggedInID = SharedPreferencesUtil.getUserId();
        for(CruUser user: cg.leaders) {
            if (loggedInID.equals(user.id))
                isLeader = true;
        }
        return isLeader;
    }
}
