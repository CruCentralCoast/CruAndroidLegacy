package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Event;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
{
    private ArrayList<Event> events;

    public EventAdapter(ArrayList<Event> events)
    {
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.mDateMonth.setText(events.get(position).startDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase());
        String monthName = String.valueOf(events.get(position).startDate.getDayOfMonth());
        holder.mDateDay.setText(monthName);
        holder.mEventName.setText(events.get(position).name);
        holder.mEventTimeframe.setText(events.get(position).startDate.format(DateTimeFormatter.ofPattern("h:m")) + " - " + events.get(position).endDate.format(DateTimeFormatter.ofPattern("h:m")));
    }

    @Override
    public int getItemCount()
    {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateMonth;
        public TextView mDateDay;
        public TextView mEventName;
        public TextView mEventTimeframe;
        public CardView cardView;

        public ViewHolder(View cardView) {
            super(cardView);
            this.cardView = (CardView) cardView;

            RelativeLayout layout = (RelativeLayout) cardView.findViewById(R.id.rootview);
            LinearLayout date = (LinearLayout) layout.findViewById(R.id.date);
            LinearLayout eventInfo = (LinearLayout) layout.findViewById(R.id.event_info);
            mDateMonth = (TextView) date.findViewById(R.id.date_month);
            mDateDay = (TextView) date.findViewById(R.id.date_day);
            mEventName = (TextView) eventInfo.findViewById(R.id.event_name);
            mEventTimeframe = (TextView) eventInfo.findViewById(R.id.event_timeframe);

        }
    }
}
