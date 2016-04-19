package org.androidcru.crucentralcoast.presentation.views.events;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.viewmodels.events.CruEventVM;

import java.util.ArrayList;

/**
 * EventsAdapter is an RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class EventsAdapter extends RecyclerView.Adapter<CruEventViewHolder>
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
        return new CruEventViewHolder(inflater.inflate(R.layout.card_event, parent, false), mLayoutManager, this);
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
        holder.vm = cruEventVM;

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


        holder.fbButton.setOnClickListener(cruEventVM.onFacebookClick());
        holder.fbButton.setImageDrawable(DrawableUtil.getTintedDrawable(context, R.drawable.ic_facebook_box_grey600_48dp, R.color.fbBlue));
        holder.mapButton.setOnClickListener(cruEventVM.onMapClick());
        holder.mapButton.setImageDrawable(DrawableUtil.getTintedDrawable(context, R.drawable.ic_map_marker_grey600_48dp, R.color.red600));
        holder.calButton.setOnClickListener(cruEventVM.onCalendarClick());

        ViewUtil.setSelected(holder.calButton,
                cruEventVM.addedToCalendar,
                R.drawable.ic_calendar_check_grey600_48dp,
                R.drawable.ic_calendar_plus_grey600_48dp,
                R.color.cal_action);

        holder.rideSharingButton.setOnClickListener(cruEventVM.onRideShareSharing());
        ViewUtil.setSelected(holder.rideSharingButton, cruEventVM.cruEvent.rideSharingEnabled,
                R.drawable.ic_car_grey600_48dp,
                R.color.ride_sharing_state);

        holder.chevronView.setImageDrawable(cruEventVM.isExpanded
                ? ContextCompat.getDrawable(context, R.drawable.ic_chevron_up_grey600_48dp)
                : ContextCompat.getDrawable(context, R.drawable.ic_chevron_down_grey600_48dp));
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
}

