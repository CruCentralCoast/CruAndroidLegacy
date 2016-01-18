package org.androidcru.crucentralcoast.presentation.views.adapters.events;

import android.app.Activity;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.modelviews.CruEventMV;
import org.androidcru.crucentralcoast.presentation.providers.CalendarProvider;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * EventsAdapter is an RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>
{
    //Pair<Event, isDescriptionVisible>
    private ArrayList<CruEventMV> mEvents;
    private Activity mParent;

    public final static String TIME_FORMATTER = "h:mm";

    private LinearLayoutManager mLayoutManager;
    private Subscriber<Pair<String, Long>> mOnCalendarWritten;

    public EventsAdapter(Activity parent, ArrayList<CruEventMV> events, LinearLayoutManager layoutManager, Subscriber<Pair<String, Long>> onCalendarWritten)
    {
        this.mParent = parent;
        this.mEvents = events;
        this.mLayoutManager = layoutManager;
        this.mOnCalendarWritten = onCalendarWritten;
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
        holder.mDateMonth.setText(mEvents.get(position).mCruEvent.mStartDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase());
        String monthName = String.valueOf(mEvents.get(position).mCruEvent.mStartDate.getDayOfMonth());
        holder.mDateDay.setText(monthName);
        holder.mEventName.setText(mEvents.get(position).mCruEvent.mName);
        holder.mEventTimeframe.setText(mEvents.get(position).mCruEvent.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
                + " - " + mEvents.get(position).mCruEvent.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));
        holder.mEventDescription.setText(mEvents.get(position).mCruEvent.mDescription);
        holder.mEventDescription.setVisibility(mEvents.get(position).mIsExpanded ? View.VISIBLE : View.GONE);

        if(mEvents.get(position).mAddedToCalendar)
        {
            holder.mEventCalendar.setText("Added to Calendar");
        }
        else
        {
            holder.mEventCalendar.setText("Add to Calendar");
        }
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
     * ViewHolder is a view representation of the model for the list
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.date_month) TextView mDateMonth;
        @Bind(R.id.date_day) TextView mDateDay;
        @Bind(R.id.event_name) TextView mEventName;
        @Bind(R.id.event_timeframe) TextView mEventTimeframe;
        @Bind(R.id.event_description) TextView mEventDescription;
        @Bind(R.id.event_calendar) Button mEventCalendar;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
            rootView.setOnClickListener(this);

            mEventCalendar.setOnClickListener(v -> {

                CruEvent selectedEvent = mEvents.get(getAdapterPosition()).mCruEvent;
                final boolean adding = !mEvents.get(getAdapterPosition()).mAddedToCalendar;
                String operation = adding ? "Add " : "Remove ";
                AlertDialog confirmDialog = new AlertDialog.Builder(mParent)
                        .setTitle(operation + selectedEvent.mName + " to your calendar?")
                        .setNegativeButton("NOPE", (dialog, which) -> {
                        })
                        .setPositiveButton("SURE", (dialog, which) -> {
                            if(adding)
                                CalendarProvider.getInstance().addEventToCalendar(mParent, selectedEvent, mOnCalendarWritten);
                            else
                                CalendarProvider.getInstance().removeEventFromCalendar(mParent, selectedEvent, mEvents.get(getAdapterPosition()).mLocalEventId, mOnCalendarWritten);
                        })
                        .create();
                confirmDialog.show();
            });
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
            if(mEventDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            mEventDescription.setVisibility(visibility);

            mEvents.get(getAdapterPosition()).mIsExpanded = (View.VISIBLE == visibility);

            notifyItemChanged(getAdapterPosition());
            mLayoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
