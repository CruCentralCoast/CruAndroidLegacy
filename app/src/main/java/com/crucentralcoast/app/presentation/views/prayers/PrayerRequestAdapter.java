package com.crucentralcoast.app.presentation.views.prayers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;

import java.util.List;

/**
 * Created by brittanyberlanga on 5/7/17.
 */

public class PrayerRequestAdapter extends RecyclerView.Adapter<PrayerRequestViewHolder> {

    private List<PrayerRequest> prayerRequests;

    public PrayerRequestAdapter(List<PrayerRequest> prayerRequests) {
        this.prayerRequests = prayerRequests;
    }

    @Override
    public PrayerRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrayerRequestViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prayer_request, parent, false));
    }

    @Override
    public void onBindViewHolder(PrayerRequestViewHolder holder, int position) {
        holder.bind(prayerRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return prayerRequests.size();
    }

    public void setPrayerRequests(List<PrayerRequest> prayerRequests) {
        this.prayerRequests = prayerRequests;
        notifyDataSetChanged();
    }
}
