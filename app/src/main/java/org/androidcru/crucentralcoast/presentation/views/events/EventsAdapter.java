package org.androidcru.crucentralcoast.presentation.views.events;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
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
        holder.eventName.setText(cruEventVM.cruEvent.mName);
        holder.eventDate.setText(cruEventVM.getDateTime());
        if(cruEventVM.cruEvent.mImage != null)
        {
            Picasso.with(holder.eventBanner.getContext())
                    .load(cruEventVM.cruEvent.mImage.mURL)
                    .fit()
                    .into(holder.eventBanner);
        }
        holder.fbButton.setOnClickListener(cruEventVM.onFacebookClick());
        holder.mapButton.setOnClickListener(cruEventVM.onMapClick());
        holder.calButton.setOnClickListener(cruEventVM.onCalendarClick());
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
        @Bind(R.id.fbButton) Button fbButton;
        @Bind(R.id.mapButton) Button mapButton;
        @Bind(R.id.calButton) Button calButton;
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

