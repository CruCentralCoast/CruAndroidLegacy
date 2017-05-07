package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Passenger;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class PassengerResultsAdapter extends RecyclerView.Adapter {

    private List<Passenger> passengers;

    public PassengerResultsAdapter(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AddPassengerViewHolder(inflater.inflate(R.layout.card_add_passenger, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddPassengerViewHolder addPassengerViewHolder = (AddPassengerViewHolder) holder;
        Passenger passenger = passengers.get(position);

        addPassengerViewHolder.mPassengerName.setText(passenger.name);
        addPassengerViewHolder.mPhone.setText(passenger.phone);
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }
}
