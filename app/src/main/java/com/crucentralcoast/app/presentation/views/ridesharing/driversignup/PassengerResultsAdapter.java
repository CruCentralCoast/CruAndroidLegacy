package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Passenger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Wong
 */

public class PassengerResultsAdapter extends RecyclerView.Adapter {

    private List<Passenger> passengers;
    private List<Passenger> selectedPassengers;
    private boolean selectable = false;
    private ItemClickListener mCallback;

    public PassengerResultsAdapter(List<Passenger> passengers, int numAvailable) {
        this.passengers = passengers;
        selectedPassengers = new ArrayList<>();

        mCallback = (boolean isChecked, int position) -> {
            if (isChecked) {
                selectedPassengers.add(passengers.get(position));

                if (selectedPassengers.size() == numAvailable) {
                    selectable = false;
                }
            }
            else {
                selectedPassengers.remove(passengers.get(position));
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AddPassengerViewHolder(inflater.inflate(R.layout.card_add_passenger, parent, false), mCallback);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddPassengerViewHolder addPassengerViewHolder = (AddPassengerViewHolder) holder;
        Passenger passenger = passengers.get(position);

        addPassengerViewHolder.passengerId = passenger.id;
        addPassengerViewHolder.mPassengerName.setText(passenger.name);
        addPassengerViewHolder.mPhone.setText(passenger.phone);

        addPassengerViewHolder.itemView.setOnClickListener(view -> {
            if (selectable) {
                addPassengerViewHolder.addPassengerToRide();
            }
        });
    }

    public List<Passenger> getSelectedPassengers() {
        return selectedPassengers;
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
