package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.BR;
import org.androidcru.crucentralcoast.databinding.CardMyridesdriverBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.MyRidesDriverVM;

import java.util.ArrayList;

/**
 * RideSharingAdapter is a RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class MyRidesDriverAdapter extends RecyclerView.Adapter<MyRidesDriverAdapter.CruRideViewHolder>
{
    private ArrayList<MyRidesDriverVM> rides;

    private LinearLayoutManager layoutManager;

    public MyRidesDriverAdapter(ArrayList<MyRidesDriverVM> rides, LinearLayoutManager layoutManager)
    {
        this.rides = rides;
        this.layoutManager = layoutManager;
    }

    /**
     * Invoked by the Adapter if a new fresh view needs to be used
     * @param parent Parent view to inflate in, provided by Android
     * @param viewType Integer representer a enumeration of heterogeneous views
     * @return CruEventViewHolder, a representation of the model for the view
     */
    @Override
    public CruRideViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardMyridesdriverBinding binding = CardMyridesdriverBinding.inflate(inflater, parent, false);

        return new CruRideViewHolder(binding.getRoot());
    }

    //TODO support events spanning multiple days (fall retreat)
    /**
     * Invoked by the Adapter if a fresh view needs configuration or an old view needs to be recycled
     * @param holder CruEventViewHolder returned by onCreateViewHolder()
     * @param position Position in the RecyclerView
     */
    @Override
    public void onBindViewHolder(CruRideViewHolder holder, int position)
    {
        MyRidesDriverVM rideVM = rides.get(position);
        holder.getBinding().setVariable(BR.ride, rideVM);
        holder.getBinding().executePendingBindings();
    }

    /**
     * Invoked by the Adapter when Android needs to know how many items are in this list
     * @return Number of items in the list
     */
    @Override
    public int getItemCount()
    {
        return rides.size();
    }

    /**
     * CruRideViewHolder is a view representation of the model for the list
     */
    public class CruRideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CruRideViewHolder(View rootView) {
            super(rootView);
            rootView.setOnClickListener(this);
        }

        public CardMyridesdriverBinding getBinding() {
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
            if(getBinding().passengerList.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            getBinding().passengerList.setVisibility(visibility);

            rides.get(getAdapterPosition()).isExpanded.set((View.VISIBLE == visibility));
            notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
