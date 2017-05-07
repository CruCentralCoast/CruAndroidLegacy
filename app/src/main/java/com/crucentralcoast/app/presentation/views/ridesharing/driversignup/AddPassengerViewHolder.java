package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    public AddPassengerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
