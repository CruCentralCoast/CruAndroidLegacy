package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinistryTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<MinistryTeam> ministryTeams;
    private Activity parent;

    public MinistryTeamsAdapter(Activity parent, List<MinistryTeam> ministryTeams)
    {
        this.parent = parent;
        this.ministryTeams = ministryTeams;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MinistryTeamHolder(inflater.inflate(R.layout.tile_team, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MinistryTeamHolder ministryTeamHolder = (MinistryTeamHolder) holder;

        // sets up the ministry team box logo for each ministry based on their position
        String image = ministryTeams.get(position).image;
        ViewUtil.setSource(ministryTeamHolder.ministryImage, image, ContextCompat.getColor(parent, R.color.cruDarkBlue),
                DrawableUtil.getDrawable(ministryTeamHolder.ministryImage.getContext(), R.drawable.default_box),
                DrawableUtil.getDrawable(ministryTeamHolder.ministryImage.getContext(), R.drawable.default_box),
                null);

        Intent intent = new Intent(parent, JoinMinistryTeamActivity.class);
        intent.putExtra(AppConstants.MINISTRY_TEAM_KEY, Parcels.wrap(ministryTeams.get(position)));
        ministryTeamHolder.ministryImage.setOnClickListener(v -> parent.startActivity(intent));
    }

    @Override
    public int getItemCount()
    {
        return ministryTeams.size();
    }

    public class MinistryTeamHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.ministry_image) ImageView ministryImage;

        public MinistryTeamHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
