package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * EventsAdapter is an RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>
{
    //Event, isDescriptionVisible
    private ArrayList<Pair<CruEvent, Boolean>> events;

    public static String TIME_FORMATTER = "h:mm";

    private LinearLayoutManager layoutManager;

    public EventsAdapter(ArrayList<CruEvent> cruEvents, LinearLayoutManager layoutManager)
    {
        this.events = new ArrayList<>();
        for (CruEvent cruEvent : cruEvents)
        {
            this.events.add(new Pair<>(cruEvent, false));
        }
        this.layoutManager = layoutManager;
    }

    /**
     * Invoked by the Adapter if a new fresh view needs to be used
     * @param parent Parent view to inflate in, provided by Android
     * @param viewType Integer representer a enumeration of heterogeneous views
     * @return ViewHolder, a representation of the model for the view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Invoked by the Adapter if a fresh view needs configuration or an old view needs to be recycled
     * @param holder ViewHolder returned by onCreateViewHolder()
     * @param position Position in the RecyclerView
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mDateMonth.setText(events.get(position).first.mStartDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase());
        String monthName = String.valueOf(events.get(position).first.mStartDate.getDayOfMonth());
        holder.mDateDay.setText(monthName);
        holder.mEventName.setText(events.get(position).first.mName);
        holder.mEventTimeframe.setText(events.get(position).first.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
                + " - " + events.get(position).first.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));
        holder.mEventDescription.setText(events.get(position).first.mDescription);
        holder.mEventDescription.setVisibility(events.get(position).second ? View.VISIBLE : View.GONE);
    }

    /**
     * Invoked by the Adapter when Android needs to know how many items are in this list
     * @return Number of items in the list
     */
    @Override
    public int getItemCount()
    {
        return events.size();
    }

    /**
     * ViewHolder is a view representation of the model for the list
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.date_month) TextView mDateMonth;
        @Bind(R.id.date_day) TextView mDateDay;
        @Bind(R.id.event_name) TextView mEventName;
        @Bind(R.id.event_timeframe) TextView mEventTimeframe;
        @Bind(R.id.event_description) TextView mEventDescription;

        public ViewHolder(View rootView) {
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
            mEventDescription.setVisibility(mEventDescription.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            events.set(getAdapterPosition(), new Pair<CruEvent, Boolean>(events.get(getAdapterPosition()).first, (mEventDescription.getVisibility() == View.VISIBLE)));
            notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
