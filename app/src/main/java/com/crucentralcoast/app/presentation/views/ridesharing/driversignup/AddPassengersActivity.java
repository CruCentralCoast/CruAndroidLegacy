package com.crucentralcoast.app.presentation.views.ridesharing.driversignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class AddPassengersActivity extends AppCompatActivity implements AddPassengersContract.View {

    @BindView(R.id.available_passengers)
    RecyclerView mAvailablePassengerList;

    private PassengerResultsAdapter mAdapter;
    private AddPassengersContract.Presenter mPresenter;
    private String eventId;
    private String rideId;
    private Ride.Gender gender;
    private int numAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_passengers);
        ButterKnife.bind(this);

        eventId = getIntent().getStringExtra(CruEvent.sId);
        rideId = getIntent().getStringExtra("rideId");
        gender = (Ride.Gender) getIntent().getSerializableExtra("gender");
        numAvailable = getIntent().getIntExtra("available", 0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAvailablePassengerList.setLayoutManager(layoutManager);
        mAdapter = new PassengerResultsAdapter(new ArrayList<>(), numAvailable);
        mAvailablePassengerList.setAdapter(mAdapter);

        mPresenter = new AddPassengersPresenter(this);
        mPresenter.loadAvailablePassengers(eventId, gender);
    }

    @OnClick(R.id.fab)
    public void addPassengers() {
        mPresenter.addPassengers(rideId, mAdapter.getSelectedPassengers());
    }

    @Override
    public void completed() {
        finish();
    }

    @Override
    public void setPresenter(AddPassengersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAvailablePassengers(List<Passenger> passengers) {
        if (passengers.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Passengers Waiting")
                    .setMessage("There doesn't seem to be any passengers waiting! " +
                            "You will receive a notification when someone joins your car.")
                    .setPositiveButton("OK", (dialog1, which) -> finish())
                    .create()
                    .show();
        }
        else {
            mAdapter.setPassengers(passengers);
        }
    }
}
