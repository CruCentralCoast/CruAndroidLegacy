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
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.observers.Observers;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.CommunityGroupViewHolder>
{
    List<CommunityGroup> communityGroups;
    FormHolder formHolder;

    public ResultsAdapter(List<CommunityGroup> communityGroups, FormHolder formHolder)
    {
        this.communityGroups = communityGroups;
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
        holder.communityGroup = communityGroups.get(position);

        holder.title.setText(communityGroups.get(position).dayOfWeek);
        holder.name.setText(communityGroups.get(position).name);

        for (int i = 0; i < communityGroups.get(position).leaders.size(); i++)
        {

            holder.leaders.setText(holder.leaders.getText().toString() + communityGroups.get(position).leaders.get(i));

            if (i != communityGroups.get(position).leaders.size() - 1)
                holder.leaders.setText(holder.leaders.getText().toString() + ", ");
        }

        holder.description.setText(communityGroups.get(position).description);

        holder.details.setVisibility(View.GONE);
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
            details.setVisibility(details.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
