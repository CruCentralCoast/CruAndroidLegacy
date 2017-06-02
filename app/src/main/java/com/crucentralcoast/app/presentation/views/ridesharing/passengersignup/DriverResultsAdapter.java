package com.crucentralcoast.app.presentation.views.ridesharing.passengersignup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.presentation.views.forms.FormContent;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverResultsAdapter extends RecyclerView.Adapter<DriverResultsAdapter.DriverResultViewHolder> {

    private FormContent formContent;
    private List<Ride> rides;
    private FormHolder formHolder;

    public DriverResultsAdapter(FormContent formContent, FormHolder holder, List<Ride> rides) {
        this.formContent = formContent;
        this.rides = rides;
        this.formHolder = holder;
    }

    @Override
    public DriverResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DriverResultViewHolder(inflater.inflate(R.layout.item_driver_result, parent, false));
    }

    @Override
    public void onBindViewHolder(DriverResultViewHolder holder, int position) {
        Ride currentRide = rides.get(position);
        holder.driverName.setText(currentRide.driverName);
        holder.rideDateTime.setText(currentRide.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER + " " + AppConstants.TIME_FORMATTER)));
        holder.distance.setText(String.format("%.2f miles away", currentRide.distance));
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public class DriverResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.driverName)
        TextView driverName;
        @BindView(R.id.rideDateTime)
        TextView rideDateTime;
        @BindView(R.id.distance)
        TextView distance;

        public DriverResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            formHolder.addDataObject(PassengerSignupActivity.SELECTED_RIDE, rides.get(getAdapterPosition()));
            formContent.onNext(formHolder);
        }
    }
}
