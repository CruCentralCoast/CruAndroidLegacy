package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

public class MinistryTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<MinistryTeam> mMinistryTeams;
    ViewGroup mParent;

    public MinistryTeamsAdapter(ArrayList<MinistryTeam> ministryTeams)
    {
        this.mMinistryTeams = ministryTeams;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        this.mParent = parent;
        return new MinistryTeamHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_subscription, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MinistryTeamHolder ministryTeamHolder = (MinistryTeamHolder) holder;

        if (mMinistryTeams.get(position).mCruImage != null)
        {
            Picasso.with(mParent.getContext())
                    .load(mMinistryTeams.get(position).mCruImage.mURL)
                    .transform(new ColorFilterTransformation(Color.parseColor("#007398")))
                    .into(ministryTeamHolder.mMinistryTeamLogo);
        }
    }

    @Override
    public int getItemCount()
    {
        return mMinistryTeams.size();
    }

    public class MinistryTeamHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mMinistryTeamLogo;

        public MinistryTeamHolder(View itemView)
        {
            super(itemView);
            mMinistryTeamLogo = (ImageView) itemView.findViewById(R.id.ministry_image);
            ((CheckBox) itemView.findViewById(R.id.checkbox)).setVisibility(View.GONE);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (mMinistryTeams.get(getAdapterPosition()).mCruImage != null)
            {
                // TODO send them to the view pager with the appropriate ministry team selected
            }
        }
    }
}
