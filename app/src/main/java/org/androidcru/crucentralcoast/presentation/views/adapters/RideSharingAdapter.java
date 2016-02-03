package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.app.Activity;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.modelviews.CruEventMV;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * RideSharingAdapter is a RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class RideSharingAdapter extends RecyclerView.Adapter<RideSharingAdapter.CruRideViewHolder>
{
    private ArrayList<CruEventMV> mEvents;
    private Activity mParent;

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    private LinearLayoutManager mLayoutManager;

    public RideSharingAdapter(Activity parent, ArrayList<CruEventMV> cruEvents, LinearLayoutManager layoutManager, Observer<Pair<String, Long>> onCalendarWritten)
    {
        this.mParent = parent;
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
    public CruRideViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ridesharing, parent, false);
        return new CruRideViewHolder(view);
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
        CruEvent cruEvent = mEvents.get(position).mCruEvent;
        CruEventMV cruEventMV = mEvents.get(position);

        if(cruEvent.mImage != null)
            Picasso.with(mParent)
                    .load(cruEvent.mImage.mURL)
                    .placeholder(R.drawable.logo_grey)
                    .fit()
                    .into(holder.banner);
        holder.eventName.setText(cruEvent.mName);
        holder.eventDate.setText(cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
                + " - " + cruEvent.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));
        holder.eventDescription.setText(cruEvent.mDescription);
        holder.eventDescription.setVisibility(cruEventMV.mIsExpanded ? View.VISIBLE : View.GONE);
        holder.launchDriver.setText("Driver");
        holder.launchPassenger.setText("Passenger");
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
     * CruRideViewHolder is a view representation of the model for the list
     */
    public class CruRideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.event_banner) ImageView banner;
        @Bind(R.id.eventName) TextView eventName;
        @Bind(R.id.eventDate) TextView eventDate;
        @Bind(R.id.eventDescription) TextView eventDescription;
        @Bind(R.id.launchPassenger) Button launchPassenger;
        @Bind(R.id.launchDriver) Button launchDriver;


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
            if(eventDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            eventDescription.setVisibility(visibility);

            mEvents.get(getAdapterPosition()).mIsExpanded = (View.VISIBLE == visibility);
            notifyItemChanged(getAdapterPosition());
            mLayoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}
