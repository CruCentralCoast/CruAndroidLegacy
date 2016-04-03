package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContent;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DriverResultsAdapter extends RecyclerView.Adapter<DriverResultsAdapter.DriverResultViewHolder>
{

    private FormContent formContent;
    private List<Ride> rides;
    private FormHolder formHolder;

    public DriverResultsAdapter(FormContent formContent, FormHolder holder, List<Ride> rides)
    {
        this.formContent = formContent;
        this.rides = rides;
        this.formHolder = holder;
    }

    @Override
    public DriverResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DriverResultViewHolder(inflater.inflate(R.layout.item_driver_result, parent, false));
    }

    @Override
    public void onBindViewHolder(DriverResultViewHolder holder, int position)
    {
        Ride currentRide = rides.get(position);
        holder.driverName.setText(currentRide.driverName);
        holder.rideDateTime.setText(currentRide.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER + " " + AppConstants.TIME_FORMATTER)));
    }

    @Override
    public int getItemCount()
    {
        return rides.size();
    }

    public class DriverResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        @Bind(R.id.driverName) TextView driverName;
        @Bind(R.id.rideDateTime) TextView rideDateTime;

        public DriverResultViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            formHolder.addDataObject(PassengerSignupActivity.SELECTED_RIDE, rides.get(getAdapterPosition()));
            formContent.onNext();
        }
    }
}
