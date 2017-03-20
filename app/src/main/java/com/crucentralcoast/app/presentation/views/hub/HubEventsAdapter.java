package com.crucentralcoast.app.presentation.views.hub;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.presentation.views.events.CruEventViewHolder;
import com.crucentralcoast.app.presentation.views.events.EventsAdapter;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class HubEventsAdapter extends EventsAdapter {

    private Context mContext;

    public HubEventsAdapter(Context context, List<CruEvent> cruEvents, LinearLayoutManager layoutManager) {
        super(cruEvents, layoutManager);
        mContext = context;
    }

    @Override
    public CruEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CruEventViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.rootView.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                mContext.getResources().getDisplayMetrics());
        holder.rootView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230,
                mContext.getResources().getDisplayMetrics());
        holder.bottomButtons.setVisibility(View.GONE);
        holder.eventAddress.setVisibility(View.GONE);
        holder.animatingLayout.setOnClickListener(null);
    }

    @Override
    public void setEvents(List<CruEvent> cruEvents) {
        super.setEvents(cruEvents);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
