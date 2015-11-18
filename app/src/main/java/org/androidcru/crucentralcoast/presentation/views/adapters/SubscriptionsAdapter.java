package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;

import java.util.ArrayList;

public class SubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionsAdapter.ViewHolder>
{
    ArrayList<MinistrySubscription> ministries;
    ViewGroup parent;

    public SubscriptionsAdapter(ArrayList<MinistrySubscription> ministries)
    {
        this.ministries = ministries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Glide.with(parent.getContext()).load(ministries.get(position).subscriptionLogo).into(holder.mSubscriptionLogo);
    }

    @Override
    public int getItemCount() {return ministries.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mSubscriptionLogo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mSubscriptionLogo = (ImageView) itemView.findViewById(R.id.ministry_image).findViewById(R.id.ministry_image);
        }
    }
}
