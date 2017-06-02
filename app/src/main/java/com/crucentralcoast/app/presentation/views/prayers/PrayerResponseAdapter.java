package com.crucentralcoast.app.presentation.views.prayers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerResponse;

import java.util.List;

/**
 * Created by brittanyberlanga on 5/22/17.
 */

public class PrayerResponseAdapter extends RecyclerView.Adapter<PrayerResponseViewHolder> {

    private List<PrayerResponse> prayerResponses;

    public PrayerResponseAdapter(List<PrayerResponse> prayerResponses) {
        this.prayerResponses = prayerResponses;
    }

    @Override
    public PrayerResponseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrayerResponseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prayer_response, parent, false));
    }

    @Override
    public void onBindViewHolder(PrayerResponseViewHolder holder, int position) {
        holder.bind(prayerResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return prayerResponses.size();
    }

    public void setPrayerResponses(List<PrayerResponse> prayerResponses) {
        if (prayerResponses == null) {
            this.prayerResponses.clear();
        } else {
            this.prayerResponses = prayerResponses;
        }
        notifyDataSetChanged();
    }
}