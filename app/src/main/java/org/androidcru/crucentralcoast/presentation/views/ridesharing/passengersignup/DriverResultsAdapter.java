package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class DriverResultsAdapter extends RecyclerView.Adapter<DriverResultsAdapter.DriverResultViewHolder>
{

    private Activity mParent;
    private ArrayList<Ride> mRides;
    private Observable<Void> mOnNextCallback;

    public DriverResultsAdapter(Activity parent, ArrayList<Ride> rides, Observable<Void> onNextCallback)
    {
        this.mParent = parent;
        this.mRides = rides;
        this.mOnNextCallback = onNextCallback;
    }

    @Override
    public DriverResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driverresult, parent, false);
        return new DriverResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DriverResultViewHolder holder, int position)
    {
        holder.mDriverName.setText(mRides.get(position).mDriverName);
        holder.mDateTime.setText(mRides.get(position).mTime.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        holder.mPickupStatus.setText("Pickup Available");
    }

    @Override
    public int getItemCount()
    {
        return mRides.size();
    }

    public class DriverResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        @Bind(R.id.driverName) TextView mDriverName;
        @Bind(R.id.rideDateTime) TextView mDateTime;
        @Bind(R.id.pickupStatus) TextView mPickupStatus;

        public DriverResultViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            mOnNextCallback.subscribe();
        }
    }
}
