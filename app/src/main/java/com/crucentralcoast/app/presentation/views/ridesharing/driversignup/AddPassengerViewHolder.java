package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.R;

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

    public String passengerId;
    public boolean isChecked = false;

    public AddPassengerViewHolder(View itemView, ItemClickListener callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mCallback = callback;
    }

    public void setSelected() {
        if (!isChecked) {
            mCheck.setVisibility(View.VISIBLE);
            mCallback.setItemChecked(isChecked = true, getAdapterPosition());
        }
        else {
            mCheck.setVisibility(View.GONE);
            mCallback.setItemChecked(isChecked = false, getAdapterPosition());
        }
    }
}
