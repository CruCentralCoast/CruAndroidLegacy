package com.crucentralcoast.app.presentation.views.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Ride;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class HomeRidesAdapter extends RecyclerView.Adapter<HomeRidesViewHolder> {
    private List<Ride> mRides;

    public HomeRidesAdapter(List<Ride> rides) {
        mRides = rides;
    }

    @Override
    public HomeRidesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new HomeRidesViewHolder(inflater.inflate(R.layout.card_home_rides, parent, false));
    }

    @Override
    public void onBindViewHolder(HomeRidesViewHolder holder, int position) {
        holder.bind(mRides.get(position));
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    public List<Ride> getRides() {
        return mRides;
    }

    public void setRides(List<Ride> rides) {
        mRides = rides;
        notifyDataSetChanged();
    }
}
