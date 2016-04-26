package org.androidcru.crucentralcoast.presentation.views.events;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.viewmodels.events.CruEventVM;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * EventsAdapter is an RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.CruEventViewHolder>
{
    private ArrayList<CruEventVM> mEvents;
    private LinearLayoutManager mLayoutManager;

    public EventsAdapter(ArrayList<CruEventVM> cruEvents, LinearLayoutManager layoutManager)
    {
        this.mEvents = cruEvents;
        this.mLayoutManager = layoutManager;
    }

    /**
     * Invoked by the Adapter if a new fresh view needs to be used
     * @param parent Parent view to inflate in, provided by Android
     * @param viewType Integer representer a enumeration of heterogeneous views
     * @return CruEventViewHolder, a representation of the model for the view
     */
    @Override
    public CruEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CruEventViewHolder(inflater.inflate(R.layout.card_event, parent, false));
    }

    //TODO support events spanning multiple days (fall retreat)
    /**
     * Invoked by the Adapter if a fresh view needs configuration or an old view needs to be recycled
     * @param holder CruEventViewHolder returned by onCreateViewHolder()
     * @param position Position in the RecyclerView
     */
    @Override
    public void onBindViewHolder(CruEventViewHolder holder, int position)
    {
        CruEventVM cruEventVM = mEvents.get(position);
        holder.eventName.setText(cruEventVM.cruEvent.name);
        holder.eventDate.setText(cruEventVM.getDateTime());
        Context context = holder.eventBanner.getContext();
        if(cruEventVM.cruEvent.image != null)
        {
            Picasso.with(context)
                    .load(cruEventVM.cruEvent.image.url)
                    .fit()
                    .into(holder.eventBanner);
        }
        else
        {
            //clear ImageView of it's old content
            holder.eventBanner.setImageResource(android.R.color.transparent);
        }


        holder.fbButton.setEnabled(cruEventVM.cruEvent.url != null && !cruEventVM.cruEvent.url.isEmpty());
        holder.fbButton.setSelected(holder.fbButton.isEnabled());
        ViewUtil.setSelected(holder.fbButton, holder.fbButton.isEnabled(), R.drawable.ic_facebook_box_grey600, R.color.facebook_state);

        holder.mapButton.setOnClickListener(cruEventVM.onMapClick());
        holder.mapButton.setImageDrawable(DrawableUtil.getTintedDrawable(context, R.drawable.ic_map_marker_grey600, R.color.red600));
        holder.calButton.setOnClickListener(cruEventVM.onCalendarClick());

        ViewUtil.setSelected(holder.calButton,
                cruEventVM.addedToCalendar,
                R.drawable.ic_calendar_check_grey600,
                R.drawable.ic_calendar_plus_grey600,
                R.color.cal_action);

        holder.rideSharingButton.setOnClickListener(cruEventVM.onRideShareSharing());
        ViewUtil.setSelected(holder.rideSharingButton, cruEventVM.cruEvent.rideSharingEnabled,
                R.drawable.ic_car_grey600,
                R.color.ride_sharing_state);

        holder.chevronView.setImageDrawable(cruEventVM.isExpanded
                ? ContextCompat.getDrawable(context, R.drawable.ic_chevron_up_grey600)
                : ContextCompat.getDrawable(context, R.drawable.ic_chevron_down_grey600));
        holder.eventDescription.setText(cruEventVM.cruEvent.description);
        holder.eventDescription.setVisibility(cruEventVM.isExpanded ? View.VISIBLE : View.GONE);
    }



    /**
     * Invoked by the Adapter when Android needs to know how many items are in this list
     * @return Number of items in the list
     */
    @Override
    public int getItemCount()
    {
        return mEvents.size();
    }

    /**
     * CruEventViewHolder is a view representation of the model for the list
     */
    public class CruEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.eventName) TextView eventName;
        @Bind(R.id.eventDate) TextView eventDate;
        @Bind(R.id.event_banner) ImageView eventBanner;
        @Bind(R.id.chevView) ImageView chevronView;
        @Bind(R.id.fbButton) ImageButton fbButton;
        @Bind(R.id.mapButton) ImageButton mapButton;
        @Bind(R.id.calButton) ImageButton calButton;
        @Bind(R.id.rideSharingButton) ImageButton rideSharingButton;
        @Bind(R.id.eventDescription) TextView eventDescription;

        public CruEventViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
            rootView.setOnClickListener(this);
        }

        /**
         * Invoked by Android if setOnClickListener() is called.
         *
         * Toggles the eventDescription Visibility if tapped, stores it in the view model so that
         * RecycledViews will work properly
         *
         * @param v View that was clicked on
         */
        @Override
        public void onClick(View v)
        {
            int visibility;
            if(eventDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            eventDescription.setVisibility(visibility);

            mEvents.get(getAdapterPosition()).isExpanded = (View.VISIBLE == visibility);
            notifyItemChanged(getAdapterPosition());
            mLayoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}

