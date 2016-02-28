package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.MyRidesDriverVM;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        return new CruRideViewHolder(inflater.inflate(R.layout.card_myridesdriver, parent, false));
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
        holder.eventName.setText(rideVM.eventName);
        holder.departureTime.setText(rideVM.getDateTime());
        holder.departureLoc.setText(rideVM.getLocation());
        holder.editOffering.setOnClickListener(rideVM.onEditOfferingClicked());
        holder.cancelOffering.setOnClickListener(rideVM.onCancelOfferingClicked());
        holder.passengerList.setText(rideVM.passengerList);
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

        @Bind(R.id.eventName) TextView eventName;
        @Bind(R.id.departureTime) TextView departureTime;
        @Bind(R.id.departureLoc) TextView departureLoc;
        @Bind(R.id.editOffering) Button editOffering;
        @Bind(R.id.cancelOffering) Button cancelOffering;
        @Bind(R.id.passengerList) TextView passengerList;

        public CruRideViewHolder(View rootView) {
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
            if(passengerList.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            passengerList.setVisibility(visibility);
            rides.get(getAdapterPosition()).isExpanded = (View.VISIBLE == visibility);
            notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
