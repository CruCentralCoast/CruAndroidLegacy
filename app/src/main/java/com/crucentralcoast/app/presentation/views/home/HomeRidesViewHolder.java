package com.crucentralcoast.app.presentation.views.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class HomeRidesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ride_type)
    TextView mRideType;
    @BindView(R.id.event_name)
    TextView mEventName;
    @BindView(R.id.event_location)
    TextView mEventLocation;
    @BindView(R.id.event_date)
    TextView mEventDate;

    private Context mContext;

    public HomeRidesViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(Ride ride) {
        if (ride.fcmId.equals(SharedPreferencesUtil.getFCMID())) {
            mRideType.setText(mContext.getString(R.string.driver));
        }
        else {
            mRideType.setText(mContext.getString(R.string.passenger));
        }
        mEventName.setText(ride.event.name);
        mEventLocation.setText(ride.event.location.toString());
        mEventDate.setText(getDateTime(ride));
    }

    private String getDateTime(Ride ride) {
        if (ride.time != null) {
            return ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                    + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER));
        }
        return "";
    }
}
