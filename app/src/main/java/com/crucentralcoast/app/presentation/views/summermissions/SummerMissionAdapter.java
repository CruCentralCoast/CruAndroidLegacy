package com.crucentralcoast.app.presentation.views.summermissions;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.SummerMission;
import com.crucentralcoast.app.presentation.customtabs.CustomTabActivityHelper;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.webview.WebviewFallback;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummerMissionAdapter extends RecyclerView.Adapter<SummerMissionAdapter.SummerMissionVH>
{
    private List<SummerMission> summerMissions;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;

    private List<Boolean> isExpanded;

    public SummerMissionAdapter(Context context, List<SummerMission> summerMissions, RecyclerView.LayoutManager layoutManager)
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

        ViewUtil.setSource(holder.missionBanner, summerMission.image,
                    0, null, null, ViewUtil.SCALE_TYPE.FIT);

        holder.chevView.setImageDrawable(isExpanded.get(position)
                ? DrawableUtil.getDrawable(context, R.drawable.ic_chevron_up_grey600)
                : DrawableUtil.getDrawable(context, R.drawable.ic_chevron_down_grey600));
        holder.missionDescription.setText(summerMission.description);
        holder.missionDescription.setVisibility(isExpanded.get(position) ? View.VISIBLE : View.GONE);
        holder.learnMore.setVisibility((summerMission.url != null && !summerMission.url.isEmpty()) ? View.VISIBLE : View.GONE);
        holder.learnMore.setOnClickListener(v -> {
            CustomTabActivityHelper.openCustomTab(
                    (Activity)context, ViewUtil.getCustomTabsIntent(v.getContext()), Uri.parse(summerMission.url),
                        new WebviewFallback());
        });
    }

    protected class SummerMissionVH extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.mission_name) TextView missionName;
        @BindView(R.id.mission_start_date) TextView missionStartDate;
        @BindView(R.id.mission_end_date) TextView missionEndDate;
        @BindView(R.id.mission_leaders) TextView missionLeaders;
        @BindView(R.id.mission_banner) ImageView missionBanner;
        @BindView(R.id.chevView) ImageView chevView;
        @BindView(R.id.learn_more) Button learnMore;
        @BindView(R.id.mission_description) TextView missionDescription;
        @BindView(R.id.animating_layout) LinearLayout animatingLayout;

        public SummerMissionVH(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            animatingLayout.setOnClickListener(this);
            ViewUtil.debounceExpandingView(animatingLayout, this);
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
