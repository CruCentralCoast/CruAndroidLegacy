package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


public class SubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionsAdapter.ViewHolder>
{
    ArrayList<MinistrySubscription> ministries;
    ViewGroup parent;
    Activity mParent;
    SharedPreferences mSharedPreferences;

    public SubscriptionsAdapter(Activity parent, ArrayList<MinistrySubscription> ministries)
    {
        this.mParent = parent;
        this.ministries = ministries;
        this.mSharedPreferences = mParent.getSharedPreferences(CruApplication.retrievePackageName(), Context.MODE_PRIVATE);
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
            // sets if the ministry has been subscribed to from shared preferences, if it hasn't been written to before, it uses the default value of false.
            ministries.get(position).mIsSubscribed = mSharedPreferences.getBoolean(ministries.get(position).mSubscriptionSlug, false);
            // sets the checkbox to checked or unchecked.
            holder.mCheckBox.setChecked(ministries.get(position).mIsSubscribed);

            if (ministries.get(position).mIsSubscribed)
            {
                Picasso.with(parent.getContext())
                        .load(ministries.get(position).mCruImage.mURL)
                        .transform(new ColorFilterTransformation(Color.parseColor("#007398")))
                        .into(holder.mSubscriptionLogo);
            }
            else
            {
                Picasso.with(parent.getContext())
                        .load(ministries.get(position).mCruImage.mURL)
                        .transform(new ColorFilterTransformation(Color.parseColor("#666062")))
                        .into(holder.mSubscriptionLogo);
            }
        }
    }

    @Override
    public int getItemCount() {return ministries.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mSubscriptionLogo;
        public CheckBox mCheckBox;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mSubscriptionLogo = (ImageView) itemView.findViewById(R.id.ministry_image);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
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
                    Picasso.with(parent.getContext())
                            .load(ministries.get(getAdapterPosition()).mCruImage.mURL)
                            .transform(new ColorFilterTransformation(Color.parseColor("#007398")))
                            .into(mSubscriptionLogo);
                    RegistrationIntentService.subscribeToMinistry(ministries.get(getAdapterPosition()).mSubscriptionSlug);

                    // stores in shared preferences that this ministry is subscribed to, key: ministry slug, value: true
                    mSharedPreferences.edit().putBoolean(ministries.get(getAdapterPosition()).mSubscriptionSlug, true).apply();
                }
                else
                {
                    ministries.get(getAdapterPosition()).mIsSubscribed = !ministries.get(getAdapterPosition()).mIsSubscribed;
                    Picasso.with(parent.getContext())
                            .load(ministries.get(getAdapterPosition()).mCruImage.mURL)
                            .transform(new ColorFilterTransformation(Color.parseColor("#666062")))
                            .into(mSubscriptionLogo);
                    RegistrationIntentService.unsubscribeToMinistry(ministries.get(getAdapterPosition()).mSubscriptionSlug);

                    // stores in shared preferences that this ministry is not subscribed to, key: ministry slug, value: false
                    mSharedPreferences.edit().putBoolean(ministries.get(getAdapterPosition()).mSubscriptionSlug, false).apply();
                }
                // set the state of the checkbox to reflect if the ministry is subscribed to.
                mCheckBox.setChecked(ministries.get(getAdapterPosition()).mIsSubscribed);
            }
        }
    }
}
