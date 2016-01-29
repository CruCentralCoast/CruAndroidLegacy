package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;


public class SubscriptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<Pair<String, MinistrySubscription>> mMinistries;
    ViewGroup mParent;
    SharedPreferences mSharedPreferences;
    
    public static final int MINISTRY_VIEW = 0;
    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    public SubscriptionsAdapter(HashMap<Campus, ArrayList<MinistrySubscription>> campusMinisryMap)
    {
        this.mMinistries = new ArrayList<>();

        for(Map.Entry<Campus, ArrayList<MinistrySubscription>> entry : campusMinisryMap.entrySet())
        {
            mMinistries.add(new Pair<>(entry.getKey().mCampusName, null));
            for(MinistrySubscription m : entry.getValue())
            {
                mMinistries.add(new Pair<>(null, m));
            }
        }

        this.mSharedPreferences = CruApplication.getSharedPreferences();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        this.mParent = parent;
        switch (viewType)
        {
            case MINISTRY_VIEW:
                return new MinistryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_subscription, parent, false));
            case HEADER_VIEW:
                return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_header, parent, false));
            default:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof MinistryHolder)
        {
            MinistryHolder ministryHolder = (MinistryHolder) holder;
            if (mMinistries.get(position).second.mCruImage != null)
            {
                // sets if the ministry has been subscribed to from shared preferences, if it hasn't been written to before, it uses the default value of false.
                mMinistries.get(position).second.mIsSubscribed = mSharedPreferences.getBoolean(mMinistries.get(position).second.mSubscriptionSlug, false);
                // sets the checkbox to checked or unchecked.
                ministryHolder.mCheckBox.setChecked(mMinistries.get(position).second.mIsSubscribed);

                if (mMinistries.get(position).second.mIsSubscribed)
                {
                    Picasso.with(mParent.getContext())
                            .load(mMinistries.get(position).second.mCruImage.mURL)
                            .transform(new ColorFilterTransformation(Color.parseColor("#007398")))
                            .into(ministryHolder.mSubscriptionLogo);
                }
                else
                {
                    Picasso.with(mParent.getContext())
                            .load(mMinistries.get(position).second.mCruImage.mURL)
                            .transform(new ColorFilterTransformation(Color.parseColor("#666062")))
                            .into(ministryHolder.mSubscriptionLogo);
                }
            }
        }
        else if(holder instanceof HeaderHolder)
        {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.mHeader.setText(mMinistries.get(position).first);
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
        return position >= mMinistries.size() || mMinistries.get(position).first != null;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder
    {

        @Bind(R.id.header) public TextView mHeader;

        public HeaderHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MinistryHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mSubscriptionLogo;
        public CheckBox mCheckBox;

        public MinistryHolder(View itemView)
        {
            super(itemView);
            mSubscriptionLogo = (ImageView) itemView.findViewById(R.id.ministry_image);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (mMinistries.get(getAdapterPosition()).second.mCruImage != null)
            {
                if (!mMinistries.get(getAdapterPosition()).second.mIsSubscribed)
                {
                    mMinistries.get(getAdapterPosition()).second.mIsSubscribed = !mMinistries.get(getAdapterPosition()).second.mIsSubscribed;
                    Picasso.with(mParent.getContext())
                            .load(mMinistries.get(getAdapterPosition()).second.mCruImage.mURL)
                            .transform(new ColorFilterTransformation(Color.parseColor("#007398")))
                            .into(mSubscriptionLogo);
                    RegistrationIntentService.subscribeToMinistry(mMinistries.get(getAdapterPosition()).second.mSubscriptionSlug);

                    // stores in shared preferences that this ministry is subscribed to, key: ministry slug, value: true
                    mSharedPreferences.edit().putBoolean(mMinistries.get(getAdapterPosition()).second.mSubscriptionSlug, true).apply();
                }
                else
                {
                    mMinistries.get(getAdapterPosition()).second.mIsSubscribed = !mMinistries.get(getAdapterPosition()).second.mIsSubscribed;
                    Picasso.with(mParent.getContext())
                            .load(mMinistries.get(getAdapterPosition()).second.mCruImage.mURL)
                            .transform(new ColorFilterTransformation(Color.parseColor("#666062")))
                            .into(mSubscriptionLogo);
                    RegistrationIntentService.unsubscribeToMinistry(mMinistries.get(getAdapterPosition()).second.mSubscriptionSlug);

                    // stores in shared preferences that this ministry is not subscribed to, key: ministry slug, value: false
                    mSharedPreferences.edit().putBoolean(mMinistries.get(getAdapterPosition()).second.mSubscriptionSlug, false).apply();
                }
                // set the state of the checkbox to reflect if the ministry is subscribed to.
                mCheckBox.setChecked(mMinistries.get(getAdapterPosition()).second.mIsSubscribed);
            }
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
