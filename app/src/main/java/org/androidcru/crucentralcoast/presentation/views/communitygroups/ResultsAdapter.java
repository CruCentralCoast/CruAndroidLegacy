package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        return new CommunityGroupViewHolder(inflater.inflate(R.layout.item_resource, parent, false));
    }

    @Override
    public void onBindViewHolder(CommunityGroupViewHolder holder, int position)
    {
        holder.title.setText(communityGroups.get(position).meetingTime);
    }

    @Override
    public int getItemCount()
    {
        return communityGroups.size();
    }

    public class CommunityGroupViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.resource_icon) ImageView icon;
        @BindView(R.id.title) TextView title;

        public CommunityGroupViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick (R.id.title)
        public void onClickTitle()
        {
            // TODO Nothing, it's complete... jk
            formHolder.next();
        }
    }
}
