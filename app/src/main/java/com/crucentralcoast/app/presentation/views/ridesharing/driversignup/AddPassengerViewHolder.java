package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.util.AnimUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private ItemClickListener mCallback;

    public String rideId;
    public String passengerId;
    public boolean isChecked = false;

    public AddPassengerViewHolder(View itemView, ItemClickListener callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mCallback = callback;
    }

    public void showCheck() {
        mCheck.setVisibility(View.VISIBLE);
        mCheck.startAnimation(AnimUtils.getGrowAnim());
    }

    public void hideCheck() {
        mCheck.startAnimation(AnimUtils.getShrinkAnim());
        mCheck.setVisibility(View.GONE);
    }

    public void addPassengerToRide() {
        if (!isChecked) {
            showCheck();
            mCallback.setItemChecked(isChecked = true, getAdapterPosition());
        }
        else {
            hideCheck();
            mCallback.setItemChecked(isChecked = false, getAdapterPosition());
        }
    }
}
