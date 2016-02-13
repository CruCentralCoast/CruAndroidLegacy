package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

//import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;

/**
 * RideSharingAdapter is a RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class MyRidesDriverAdapter extends RecyclerView.Adapter<MyRidesDriverAdapter.MyRideDriverViewHolder>
{
//    private ArrayList<CruEventMV> mEvents; //DELETE THIS
    private ArrayList<Ride> rides;
    private Activity mParent;
    private boolean mIsExpanded;

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    private LinearLayoutManager mLayoutManager;

    //DELETE THIS CONSTRUCTOR
//    public MyRidesDriverAdapter(Activity parent, ArrayList<CruEventMV> cruEvents, LinearLayoutManager layoutManager, Observer<Pair<String, Long>> onCalendarWritten)
//    {
//        this.mParent = parent;
//        this.mEvents = cruEvents;
//        this.mLayoutManager = layoutManager;
//    }

    public MyRidesDriverAdapter(Activity parent, ArrayList<Ride> rides, LinearLayoutManager layoutManager)
    {
        this.mParent = parent;
        this.rides = rides;
        this.mLayoutManager = layoutManager;
    }

    /**
     * Invoked by the Adapter if a new fresh view needs to be used
     * @param parent Parent view to inflate in, provided by Android
     * @param viewType Integer representer a enumeration of heterogeneous views
     * @return CruEventViewHolder, a representation of the model for the view
     */
    @Override
    public MyRideDriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_rides_driver, parent, false);
        return new MyRideDriverViewHolder(view);
    }

//    //TODO support events spanning multiple days (fall retreat)
//    /**
//     * Invoked by the Adapter if a fresh view needs configuration or an old view needs to be recycled
//     * @param holder CruEventViewHolder returned by onCreateViewHolder()
//     * @param position Position in the RecyclerView
//     */
//    @Override
//    public void onBindViewHolder(MyRideDriverViewHolder holder, int position)
//    {
//        CruEvent cruEvent = mEvents.get(position).mCruEvent;
//        CruEventMV cruEventMV = mEvents.get(position);
//
//        holder.eventName.setText(cruEvent.mName);
//        holder.eventDate.setText(cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
//                + " " + cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
//                + " - " + cruEvent.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));
//        holder.eventDescription.setText(cruEvent.mDescription);
//        holder.eventDescription.setVisibility(cruEventMV.mIsExpanded ? View.VISIBLE : View.GONE);
//
//        holder.chevView.setImageDrawable(
//                ContextCompat.getDrawable(mParent,
//                        holder.eventDescription.getVisibility() == View.VISIBLE ? R.drawable.ic_chevron_up_grey600_48dp
//                                : R.drawable.ic_chevron_down_grey600_48dp));
//
//        holder.launchDriver.setText(R.string.driver);
//
//        holder.launchDriver.setOnClickListener(v -> mParent.startActivity(new Intent(mParent, DriverSignupActivity.class)));
//    }

//    @SerializedName("driverName") public String driverName;
//    @SerializedName("driverNumber") public String driverNumber;
//    @SerializedName("gender") public String gender;
//    @SerializedName("event") public String eventId;
//    @SerializedName("time") public ZonedDateTime time;
//    @SerializedName("location") public Location location;
//    @SerializedName("passengers") public ArrayList<String> passengers;
    //TODO support events spanning multiple days (fall retreat)
    /**
     * Invoked by the Adapter if a fresh view needs configuration or an old view needs to be recycled
     * @param holder CruEventViewHolder returned by onCreateViewHolder()
     * @param position Position in the RecyclerView
     */
    @Override
    public void onBindViewHolder(MyRideDriverViewHolder holder, int position)
    {
        Ride ride = rides.get(position);

        holder.eventName.setText(ride.eventId);
        holder.departureLoc.setText(ride.location.toString());
        holder.departureTime.setText("Departing: " + ride.time.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));

        holder.eventDescription.setText(formatPassengerList(ride.passengers));
        holder.eventDescription.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
        holder.chevView.setImageDrawable(
                ContextCompat.getDrawable(mParent,
                        holder.eventDescription.getVisibility() == View.VISIBLE ? R.drawable.ic_chevron_up_grey600_48dp
                                : R.drawable.ic_chevron_down_grey600_48dp));

        holder.editOffering.setText(R.string.edit);
        //holder.editOffering.setOnClickListener(v -> mParent.startActivity(new Intent(mParent, DriverSignupActivity.class)));
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
    public class MyRideDriverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.eventName) TextView eventName;
        @Bind(R.id.departureTime) TextView departureTime;
        @Bind(R.id.departureLoc) TextView departureLoc;
        @Bind(R.id.chevView) ImageView chevView;
        @Bind(R.id.editOffering) Button editOffering;
        @Bind(R.id.eventDescription) TextView eventDescription;


        public MyRideDriverViewHolder(View rootView) {
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

            mIsExpanded = (View.VISIBLE == visibility);
            notifyItemChanged(getAdapterPosition());
            mLayoutManager.scrollToPosition(getAdapterPosition());
        }
    }

    private String formatPassengerList(ArrayList<String> passengerList)
    {
        String list = "";

        for (String passenger : passengerList)
        {
            list += passenger + "\n";
        }

        return list;
    }
}
