package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.util.AnimUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class AddPassengerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.passenger_name)
    TextView mPassengerName;
    @BindView(R.id.phone)
    TextView mPhone;
    @BindView(R.id.check)
    ImageView mCheck;

    public String rideId;
    public String passengerId;

    public AddPassengerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(view -> addPassengerToRide());
    }

    private void showCheck() {
        mCheck.setVisibility(View.VISIBLE);
        mCheck.startAnimation(AnimUtils.getGrowAnim());
    }

    private void addPassengerToRide() {
        RideProvider.addPassengerToRide(rideId, passengerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aVoid -> showCheck(),
                        Timber::e,
                        () -> Timber.i("Successfully added to ride")
                );
    }
}
