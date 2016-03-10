package org.androidcru.crucentralcoast.presentation.views.summermissions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SummerMissionAdapter extends RecyclerView.Adapter<SummerMissionAdapter.SummerMissionVH>
{
    private ArrayList<SummerMission> summerMissions;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;

    private ArrayList<Boolean> isExpanded;

    public SummerMissionAdapter(Context context, ArrayList<SummerMission> summerMissions, RecyclerView.LayoutManager layoutManager)
    {
        this.context = context;
        this.summerMissions = summerMissions;
        this.layoutManager = layoutManager;
        this.isExpanded = new ArrayList<>(Arrays.asList(new Boolean[summerMissions.size()]));
        Collections.fill(isExpanded, Boolean.FALSE);
    }

    @Override
    public SummerMissionVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SummerMissionVH(inflater.inflate(R.layout.card_summer_missions, parent, false));
    }

    @Override
    public int getItemCount()
    {
        return summerMissions.size();
    }

    @Override
    public void onBindViewHolder(SummerMissionVH holder, int position)
    {
        SummerMission summerMission = summerMissions.get(position);
        holder.missionName.setText(summerMission.name);
        holder.missionStartDate.setText(context.getResources().getString(R.string.until,
                summerMission.startDate.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER_NO_DAY)),
                summerMission.endDate.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER_NO_DAY))));
        holder.missionLeaders.setText(context.getResources().getString(R.string.mission_leaders,
                summerMission.leaders));
        holder.missionLeaders.setVisibility(isExpanded.get(position) && summerMission.leaders != null ? View.VISIBLE : View.GONE);
        if(summerMission.image != null)
        {
            Picasso.with(context)
                    .load(summerMission.image.url)
                    .placeholder(R.drawable.logo_grey)
                    .fit()
                    .into(holder.missionBanner);
        }
        else
        {
            Picasso.with(context)
                    .load(R.drawable.logo_grey)
                    .fit()
                    .into(holder.missionBanner);
        }

        holder.chevView.setImageDrawable(isExpanded.get(position)
                ? ContextCompat.getDrawable(context, R.drawable.ic_chevron_up_grey600_48dp)
                : ContextCompat.getDrawable(context, R.drawable.ic_chevron_down_grey600_48dp));
        holder.missionDescription.setText(summerMission.description);
        holder.missionDescription.setVisibility(isExpanded.get(position) ? View.VISIBLE : View.GONE);
        holder.learnMore.setVisibility(summerMission.url != null ? View.VISIBLE : View.GONE);
        holder.learnMore.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(summerMission.url));
            context.startActivity(i);
        });
    }

    protected class SummerMissionVH extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @Bind(R.id.mission_name) TextView missionName;
        @Bind(R.id.mission_start_date) TextView missionStartDate;
        @Bind(R.id.mission_end_date) TextView missionEndDate;
        @Bind(R.id.mission_leaders) TextView missionLeaders;
        @Bind(R.id.mission_banner) ImageView missionBanner;
        @Bind(R.id.chevView) ImageView chevView;
        @Bind(R.id.learn_more) Button learnMore;
        @Bind(R.id.mission_description) TextView missionDescription;

        public SummerMissionVH(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int visibility;
            if(missionDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            missionDescription.setVisibility(visibility);

            isExpanded.set(getAdapterPosition(), (View.VISIBLE == visibility));
            notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
