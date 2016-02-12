package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.BR;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.databinding.TileTeamBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ministryteams.MinistryTeamVM;

import java.util.ArrayList;

public class MinistryTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<MinistryTeamVM> mMinistryTeams;

    public MinistryTeamsAdapter(ArrayList<MinistryTeam> ministryTeams)
    {
        this.mMinistryTeams = new ArrayList<>();
        for(MinistryTeam ministryTeam : ministryTeams)
        {
            mMinistryTeams.add(new MinistryTeamVM(ministryTeam));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MinistryTeamHolder(TileTeamBinding.inflate(inflater, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MinistryTeamHolder ministryTeamHolder = (MinistryTeamHolder) holder;
        ministryTeamHolder.getBinding().setVariable(BR.team, mMinistryTeams.get(position));
    }

    @Override
    public int getItemCount()
    {
        return mMinistryTeams.size();
    }

    public class MinistryTeamHolder extends RecyclerView.ViewHolder
    {
        public MinistryTeamHolder(View itemView)
        {
            super(itemView);
        }

        public TileTeamBinding getBinding()
        {
            return DataBindingUtil.getBinding(itemView);
        }
    }
}
