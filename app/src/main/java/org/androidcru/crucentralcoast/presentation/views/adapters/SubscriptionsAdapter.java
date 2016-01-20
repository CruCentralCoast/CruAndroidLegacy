package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


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
        if (ministries.get(position).mCruImage != null)
        {
            if (ministries.get(position).mIsSubscribed)
            {
                Picasso.with(parent.getContext()).load(ministries.get(position).mCruImage.mURL).transform(new ColorFilterTransformation(Color.parseColor("#007398"))).into(holder.mSubscriptionLogo);
            }
            else
            {
                Picasso.with(parent.getContext()).load(ministries.get(position).mCruImage.mURL).transform(new ColorFilterTransformation(Color.parseColor("#666062"))).into(holder.mSubscriptionLogo);
            }
        }
    }

    @Override
    public int getItemCount() {return ministries.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mSubscriptionLogo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mSubscriptionLogo = (ImageView) itemView.findViewById(R.id.ministry_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (ministries.get(getAdapterPosition()).mCruImage != null)
            {
                if (!ministries.get(getAdapterPosition()).mIsSubscribed)
                {
                    ministries.get(getAdapterPosition()).mIsSubscribed = !ministries.get(getAdapterPosition()).mIsSubscribed;
                    Picasso.with(parent.getContext()).load(ministries.get(getAdapterPosition()).mCruImage.mURL).transform(new ColorFilterTransformation(Color.parseColor("#007398"))).into(mSubscriptionLogo);
                    //RegistrationIntentService.subscribeToMinistry(ministries.get(getAdapterPosition()).mSubscriptionSlug);
                }
                else
                {
                    ministries.get(getAdapterPosition()).mIsSubscribed = !ministries.get(getAdapterPosition()).mIsSubscribed;
                    Picasso.with(parent.getContext()).load(ministries.get(getAdapterPosition()).mCruImage.mURL).transform(new ColorFilterTransformation(Color.parseColor("#666062"))).into(mSubscriptionLogo);
                    //RegistrationIntentService.unsubscribeToMinistry(ministries.get(getAdapterPosition()).mSubscriptionSlug);
                }
            }
        }
    }
}
