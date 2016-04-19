package org.androidcru.crucentralcoast.presentation.views.events;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.viewmodels.events.CruEventVM;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * CruEventViewHolder is a view representation of the model for the list
 */
public class CruEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @Bind(R.id.eventName)
    TextView eventName;
    @Bind(R.id.eventDate) TextView eventDate;
    @Bind(R.id.event_banner)
    ImageView eventBanner;
    @Bind(R.id.chevView) ImageView chevronView;
    @Bind(R.id.fbButton)
    ImageButton fbButton;
    @Bind(R.id.mapButton) ImageButton mapButton;
    @Bind(R.id.calButton) ImageButton calButton;
    @Bind(R.id.rideSharingButton) ImageButton rideSharingButton;
    @Bind(R.id.eventDescription) TextView eventDescription;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    CruEventVM vm;

    public CruEventViewHolder(View rootView, RecyclerView.LayoutManager layoutManager,
                              RecyclerView.Adapter adapter)
    {
        super(rootView);
        this.layoutManager = layoutManager;
        this.adapter = adapter;

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
        if(vm != null)
        {
            int visibility;
            if (eventDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            } else
            {
                visibility = View.VISIBLE;
            }
            eventDescription.setVisibility(visibility);

            vm.isExpanded = (View.VISIBLE == visibility);
            adapter.notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
