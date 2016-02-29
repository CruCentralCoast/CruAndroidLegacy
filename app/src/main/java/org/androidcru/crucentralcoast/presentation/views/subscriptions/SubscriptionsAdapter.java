package org.androidcru.crucentralcoast.presentation.views.subscriptions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.presentation.viewmodels.subscriptions.MinistrySubscriptionVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


public class SubscriptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<MinistrySubscriptionVM> mMinistries;
    
    public static final int MINISTRY_VIEW = 0;
    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    public SubscriptionsAdapter(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        this.mMinistries = new ArrayList<>();

        convertSubscriptions(campusMinistryMap);
    }

    private void convertSubscriptions(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinisryMap)
    {
        for(Map.Entry<Campus, ArrayList<MinistrySubscription>> entry : campusMinisryMap.entrySet())
        {
            mMinistries.add(new MinistrySubscriptionVM(entry.getKey().mCampusName, null));
            for(MinistrySubscription m : entry.getValue())
            {
                mMinistries.add(new MinistrySubscriptionVM(null, m));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case MINISTRY_VIEW:
                View tileView = inflater.inflate(R.layout.tile_subscription, parent, false);
                return new MinistrySubscriptionHolder(tileView);
            case HEADER_VIEW:
                View headerView = inflater.inflate(R.layout.subscription_header, parent, false);
                return new HeaderHolder(headerView);
            default:
                return new FooterHolder(inflater.inflate(R.layout.blank_footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position < mMinistries.size())
        {
            MinistrySubscriptionVM ministrySubscriptionVM = mMinistries.get(position);
            if (holder instanceof MinistrySubscriptionHolder)
            {
                MinistrySubscriptionHolder ministrySubscriptionHolder = (MinistrySubscriptionHolder) holder;

                boolean isChecked = ministrySubscriptionVM.getIsSubscribed();
                ministrySubscriptionHolder.checkBox.setChecked(isChecked);
                if(ministrySubscriptionVM.ministry.mCruImage != null)
                {
                    Context context = ministrySubscriptionHolder.ministryImage.getContext();
                    Picasso.with(context)
                            .load(ministrySubscriptionVM.ministry.mCruImage.mURL)
                            .transform(new ColorFilterTransformation(ContextCompat.getColor(context, isChecked ? R.color.cruDarkBlue : R.color.cruGray)))
                            .into(ministrySubscriptionHolder.ministryImage);
                }


            } else if (holder instanceof HeaderHolder)
            {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.header.setText(ministrySubscriptionVM.campusName);
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position >= mMinistries.size() ? FOOTER_VIEW : (isHeader(position) ? HEADER_VIEW : MINISTRY_VIEW);
    }

    @Override
    public int getItemCount() {return mMinistries.size() + 1;}

    public boolean isHeader(int position)
    {
        return position >= mMinistries.size() || mMinistries.get(position).campusName != null;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.header) TextView header;

        public HeaderHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class MinistrySubscriptionHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.ministry_image) ImageView ministryImage;
        @Bind(R.id.checkbox) CheckBox checkBox;

        public MinistrySubscriptionHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tile_subscription)
        public void onClick(View v)
        {
            MinistrySubscriptionVM ministrySubscriptionVM = mMinistries.get(getAdapterPosition());
            ministrySubscriptionVM.setIsSubscribed(!ministrySubscriptionVM.getIsSubscribed());
            notifyItemChanged(getAdapterPosition());
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder
    {

        public FooterHolder(View itemView)
        {
            super(itemView);
        }
    }
}
