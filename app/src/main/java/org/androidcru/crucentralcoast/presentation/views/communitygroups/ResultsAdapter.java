package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.data.providers.CommunityGroupProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.observers.Observers;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.CommunityGroupViewHolder>
{
    List<ExpandableState<CommunityGroup>> communityGroups;
    FormHolder formHolder;

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
        return new CommunityGroupViewHolder(inflater.inflate(R.layout.item_community_group_result, parent, false));
    }

    @Override
    public void onBindViewHolder(CommunityGroupViewHolder holder, int position)
    {
        CommunityGroup cg = communityGroups.get(position).model;

        holder.communityGroup = cg;

        holder.title.setText(cg.dayOfWeek + ", " + cg.meetingTime.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT)));
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

        public CommunityGroupViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick (R.id.community_group_result_view)
        public void onTap()
        {
            // inverts the visibility of the description field
            communityGroups.get(getAdapterPosition()).isExpanded = !communityGroups.get(getAdapterPosition()).isExpanded;
            notifyItemChanged(getAdapterPosition());
        }

        @OnClick (R.id.join_community_group_button)
        public void onJoin()
        {
            // writes the community group so the correct leaders will be setup.
            formHolder.addDataObject(AppConstants.COMMUNITY_GROUP, communityGroup);

            //send push notification to CG leaders
            //CommunityGroupProvider.joinCommunityGroup(Observers.empty(),));

            formHolder.next();
        }
    }
}
