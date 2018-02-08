package com.crucentralcoast.app.presentation.views.communitygroups;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Campus;
import com.crucentralcoast.app.data.models.MinistrySubscription;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.presentation.views.subscriptions.Item;
import com.crucentralcoast.app.presentation.views.subscriptions.MinistrySubscriptionHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

public class MinistrySelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public ArrayList<Item<Campus, MinistrySubscription>> ministries;
    private FormHolder formHolder;

    public static final int MINISTRY_VIEW = 0;
    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    public MinistrySelectionAdapter(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap, FormHolder formHolder)
    {
        this.formHolder = formHolder;
        convertSubscriptions(campusMinistryMap);
    }

    private void convertSubscriptions(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinistryMap)
    {
        ArrayList<android.support.v4.util.Pair<Campus, Integer>> sortableList = new ArrayList<>();
        for(Map.Entry<Campus, ArrayList<MinistrySubscription>> entry : campusMinistryMap.entrySet())
        {
            sortableList.add(new android.support.v4.util.Pair(entry.getKey(), entry.getValue().size()));
        }

        // This sorts the subscription page so it shows the campus with the most to least ministries
        Collections.sort(sortableList, new Comparator<android.support.v4.util.Pair<Campus, Integer>>()
        {
            @Override
            public int compare(android.support.v4.util.Pair<Campus, Integer> lhs, android.support.v4.util.Pair<Campus, Integer> rhs)
            {
                return rhs.second - lhs.second;
            }
        });

        if(ministries == null)
            ministries = new ArrayList<>();
        else
            ministries.clear();

        //adds each campus and each ministry in that campus to the ministries ArrayList in order
        for (android.support.v4.util.Pair<Campus, Integer> campusPair : sortableList)
        {
            if (campusPair.first != null) {
                ministries.add(new Item<>(campusPair.first, null));


                for (MinistrySubscription m : campusMinistryMap.get(campusPair.first))
                    ministries.add(new Item(null, m));
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
                return new CommunityGroupSubscriptionHolder(tileView, this);
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
                ministrySubscriptionHolder.bindUI(item.item);
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
        return position >= ministries.size() ? FOOTER_VIEW : (isHeader(position) ? HEADER_VIEW : MINISTRY_VIEW);
    }

    @Override
    public int getItemCount() {return ministries.size() + 1;}

    public boolean isHeader(int position)
    {
        return position >= ministries.size() || ministries.get(position).isHeader();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.header)
        TextView header;

        public HeaderHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class CommunityGroupSubscriptionHolder extends MinistrySubscriptionHolder
    {

        public CommunityGroupSubscriptionHolder(View itemView, RecyclerView.Adapter adapter)
        {
            super(itemView, adapter);
        }

        @Override
        public void bindUI(MinistrySubscription model)
        {
            this.model = model;
            checkBox.setVisibility(View.GONE);
            if(model.image != null && !model.image.isEmpty())
            {
                Context context = ministryImage.getContext();
                Picasso.with(context)
                        .load(model.image)
                        .transform(new ColorFilterTransformation(ContextCompat.getColor(context, R.color.cruGray)))
                        .into(ministryImage);
            }
            else
            {
                ministryImage.setImageResource(R.drawable.default_box);
            }

        }

        @Override
        @OnClick(R.id.tile_subscription)
        public void onClick(View v)
        {
            formHolder.addDataObject("ministry", model.subscriptionId);
            formHolder.next();
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
