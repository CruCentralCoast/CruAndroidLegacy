package com.crucentralcoast.app.presentation.views.subscriptions;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Campus;
import com.crucentralcoast.app.data.models.MinistrySubscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Connor Batch
 *
 * Sets up the ViewHolder's for each Ministry tile on the Subscriptions page.
 */
public class SubscriptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public ArrayList<Item<Campus, MinistrySubscription>> ministries;

    public static final int MINISTRY_VIEW = 0;
    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    public SubscriptionsAdapter(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        convertSubscriptions(campusMinistryMap);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case MINISTRY_VIEW:
                View tileView = inflater.inflate(R.layout.tile_subscription, parent, false);
                return new MinistrySubscriptionHolder(tileView, this);
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
        if(position < ministries.size())
        {
            Item<Campus, MinistrySubscription> item = ministries.get(position);
            if (holder instanceof MinistrySubscriptionHolder)
            {
                MinistrySubscriptionHolder ministrySubscriptionHolder = (MinistrySubscriptionHolder) holder;
                if(item.item != null) {
                    ministrySubscriptionHolder.bindUI(item.item);
                }

            }
            else if (holder instanceof HeaderHolder)
            {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.header.setText(item.header.campusName);
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position >= ministries.size() ? FOOTER_VIEW
                : (isHeader(position, ministries)
                    ? HEADER_VIEW
                    : MINISTRY_VIEW);
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

    private void convertSubscriptions(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        ArrayList<Pair<Campus, Integer>> sortableList = new ArrayList<>();
        for(Map.Entry<Campus, ArrayList<MinistrySubscription>> entry : campusMinistryMap.entrySet())
        {
            sortableList.add(new Pair(entry.getKey(), entry.getValue().size()));
        }

        // This sorts the subscription page so it shows the campus with the most to least ministries
        Collections.sort(sortableList, new Comparator<Pair<Campus, Integer>>()
        {
            @Override
            public int compare(Pair<Campus, Integer> lhs, Pair<Campus, Integer> rhs)
            {
                return rhs.second - lhs.second;
            }
        });

        if(ministries == null)
            ministries = new ArrayList<>();
        else
            ministries.clear();

        //adds each campus and each ministry in that campus to the ministries ArrayList in order
        for (Pair<Campus, Integer> campusPair : sortableList)
        {
            ministries.add(new Item<>(campusPair.first, null));
            for (MinistrySubscription m : campusMinistryMap.get(campusPair.first))
                ministries.add(new Item(null, m));
        }

    }

    public static boolean isHeader(int position, List<Item<Campus, MinistrySubscription>> ministries)
    {
        return position >= ministries.size() || ministries.get(position).isHeader();
    }

    public class FooterHolder extends RecyclerView.ViewHolder
    {
        public FooterHolder(View itemView)
        {
            super(itemView);
        }
    }
}
