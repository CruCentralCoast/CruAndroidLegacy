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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

/**
 * @author Connor Batch
 *
 * Sets up the ViewHolder's for each Ministry tile on the Subscriptions page.
 */
public class SubscriptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public ArrayList<MinistrySubscriptionVM> ministries;

    public SubscriptionsAdapter(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        this.ministries = SubscriptionsSorter.convertSubscriptions(campusMinistryMap);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case SubscriptionsSorter.MINISTRY_VIEW:
                View tileView = inflater.inflate(R.layout.tile_subscription, parent, false);
                return new MinistrySubscriptionHolder(tileView);
            case SubscriptionsSorter.HEADER_VIEW:
                View headerView = inflater.inflate(R.layout.subscription_header, parent, false);
                return new HeaderHolder(headerView);
            default:
                return new FooterHolder(inflater.inflate(R.layout.blank_footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position < ministries.size())
        {
            MinistrySubscriptionVM ministrySubscriptionVM = ministries.get(position);
            if (holder instanceof MinistrySubscriptionHolder)
            {
                MinistrySubscriptionHolder ministrySubscriptionHolder = (MinistrySubscriptionHolder) holder;

                boolean isChecked = ministrySubscriptionVM.getIsSubscribed();
                ministrySubscriptionHolder.checkBox.setChecked(isChecked);
                if(ministrySubscriptionVM.ministry.image != null)
                {
                    Context context = ministrySubscriptionHolder.ministryImage.getContext();
                    Picasso.with(context)
                            .load(ministrySubscriptionVM.ministry.image.url)
                            .transform(new ColorFilterTransformation(ContextCompat.getColor(context, isChecked ? R.color.cruDarkBlue : R.color.cruGray)))
                            .into(ministrySubscriptionHolder.ministryImage);
                }
                else
                {
                    ministrySubscriptionHolder.ministryImage.setImageResource(R.drawable.default_box);
                }


            }
            else if (holder instanceof HeaderHolder)
            {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.header.setText(ministrySubscriptionVM.campusName);
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position >= ministries.size() ? SubscriptionsSorter.FOOTER_VIEW
                : (SubscriptionsSorter.isHeader(position, ministries)
                    ? SubscriptionsSorter.HEADER_VIEW
                    : SubscriptionsSorter.MINISTRY_VIEW);
    }

    @Override
    public int getItemCount() {return ministries.size() + 1;}


    public class HeaderHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.header) TextView header;

        public HeaderHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class MinistrySubscriptionHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.ministry_image) ImageView ministryImage;
        @BindView(R.id.checkbox) CheckBox checkBox;

        public MinistrySubscriptionHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tile_subscription)
        public void onClick(View v)
        {
            MinistrySubscriptionVM ministrySubscriptionVM = ministries.get(getAdapterPosition());
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
