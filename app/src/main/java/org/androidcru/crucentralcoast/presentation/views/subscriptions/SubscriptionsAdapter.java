package org.androidcru.crucentralcoast.presentation.views.subscriptions;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.BR;
import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.databinding.SubscriptionHeaderBinding;
import org.androidcru.crucentralcoast.databinding.TileSubscriptionBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.subscriptions.MinistrySubscriptionVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
                TileSubscriptionBinding tileBinding = TileSubscriptionBinding.inflate(inflater, parent, false);
                return new MinistrySubscriptionHolder(tileBinding.getRoot());
            case HEADER_VIEW:
                SubscriptionHeaderBinding headerBinding = SubscriptionHeaderBinding.inflate(inflater, parent, false);
                return new HeaderHolder(headerBinding.getRoot());
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
                ministrySubscriptionHolder.getBinding().setVariable(BR.subscription, ministrySubscriptionVM);
            } else if (holder instanceof HeaderHolder)
            {
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.getBinding().setVariable(BR.subscription, ministrySubscriptionVM);
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
        public HeaderHolder(View itemView)
        {
            super(itemView);
        }

        public SubscriptionHeaderBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }

    }

    public class MinistrySubscriptionHolder extends RecyclerView.ViewHolder
    {

        public MinistrySubscriptionHolder(View itemView)
        {
            super(itemView);
        }

        public TileSubscriptionBinding getBinding()
        {
            return DataBindingUtil.getBinding(itemView);
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
