package org.androidcru.crucentralcoast.presentation.views.events;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;

import java.util.ArrayList;

/**
 * EventsAdapter is an RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class EventsAdapter extends RecyclerView.Adapter<CruEventViewHolder>
{
    private ArrayList<ExpandableState<CruEvent>> events;
    private LinearLayoutManager layoutManager;

    public EventsAdapter(ArrayList<CruEvent> cruEvents, LinearLayoutManager layoutManager)
    {
        this.events = new ArrayList<>();
        for(CruEvent cruEvent : cruEvents)
        {
            events.add(new ExpandableState<>(cruEvent));
        }
        this.layoutManager = layoutManager;
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
        return new CruEventViewHolder(inflater.inflate(R.layout.card_event, parent, false), this, layoutManager);
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
        holder.bind(events.get(position));
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
}

