package com.crucentralcoast.app.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.queries.Query;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.presentation.util.DividerItemDecoration;
import com.crucentralcoast.app.presentation.views.forms.FormContentListFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class DriverResultsFragment extends FormContentListFragment {
    @BindView(R.id.informational_text)
    protected TextView informationalText;

    private Query query;
    private ZonedDateTime selectedTime;
    private List<Ride> results;
    private Passenger passenger;

    private FormHolder formHolder;
    private Observer<List<Ride>> rideResultsObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        informationalText.setText(R.string.no_rides);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        helper.swipeRefreshLayout.setOnRefreshListener(this::getRides);
    }

    private void getRides() {
        helper.swipeRefreshLayout.setRefreshing(true);
        results.clear();
        RideProvider.searchRides(this, rideResultsObserver, query, passenger.location.geo, selectedTime);
    }

    private void handleResults(List<Ride> results) {
        this.results = results;
        helper.recyclerView.setAdapter(new DriverResultsAdapter(this, formHolder, results));
    }

    @Override
    public View onEmpty(int layoutId) {
        new AlertDialog.Builder(getContext())
                .setTitle("No Drivers Available")
                .setMessage("There doesn't seem to be any drivers available for your location! " +
                        "Would you like to request a ride anyways? You will receive a notification" +
                        " when a closer driver chooses you.")
                .setPositiveButton("YES", (dialog, which) -> {
                    // We want to add the passenger to the database to be picked up later by a driver
                    formHolder.addDataObject("fromDialog", true);
                    onNext(formHolder);
                })
                .setNegativeButton("NO", (dialog, which) -> {
                    // Close dialog
                })
                .create()
                .show();
        return super.onEmpty(layoutId);
    }

    @Override
    public void onNext(FormHolder formHolder) {
        formHolder.addDataObject("passenger", passenger);
        super.onNext(formHolder);
    }

    @Override
    public void setupData(FormHolder formHolder) {
        this.formHolder = formHolder;
        formHolder.setTitle(getString(R.string.passenger_pick_driver));
        passenger = (Passenger) formHolder.getDataObject("passenger");
        query = (Query) formHolder.getDataObject(PassengerSignupActivity.QUERY);
        selectedTime = (ZonedDateTime) formHolder.getDataObject(PassengerSignupActivity.SELECTED_TIME);

        results = new ArrayList<>();
        rideResultsObserver = createListObserver(R.layout.empty_with_alert, this::handleResults);

        formHolder.setNavigationVisibility(View.VISIBLE);
        formHolder.setNextVisibility(View.GONE);
        formHolder.setPreviousVisibility(View.VISIBLE);
        getRides();
    }
}
