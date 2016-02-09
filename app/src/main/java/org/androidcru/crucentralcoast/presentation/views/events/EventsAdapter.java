package org.androidcru.crucentralcoast.presentation.views.events;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//sometimes this import will be red but it will still compile
import org.androidcru.crucentralcoast.BR;
import org.androidcru.crucentralcoast.databinding.CardEventBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.events.CruEventVM;

import java.util.ArrayList;

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
        CardEventBinding binding = CardEventBinding.inflate(inflater, parent, false);

        return new CruEventViewHolder(binding.getRoot());
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
        holder.getBinding().setVariable(BR.event, cruEventVM);
        holder.getBinding().executePendingBindings();
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

        public CruEventViewHolder(View rootView) {
            super(rootView);
            rootView.setOnClickListener(this);
        }

        public CardEventBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
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
            if(getBinding().eventDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            getBinding().eventDescription.setVisibility(visibility);

            mEvents.get(getAdapterPosition()).isExpanded.set((View.VISIBLE == visibility));
            notifyItemChanged(getAdapterPosition());
            mLayoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}

