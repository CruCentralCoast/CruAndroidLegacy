package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentListFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class DriverResultsFragment extends FormContentListFragment
{
    @BindView(R.id.informational_text) protected TextView informationalText;

    private Query query;
    private ZonedDateTime selectedTime;
    private List<Ride> results;
    private LatLng passengerLocation;

    private FormHolder formHolder;
    private Observer<List<Ride>> rideResultsObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        informationalText.setText(R.string.no_rides);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        helper.swipeRefreshLayout.setOnRefreshListener(() -> getRides());
    }


    private void getRides()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        results.clear();
        RideProvider.searchRides(this, rideResultsObserver, query,
                new double[]{passengerLocation.latitude, passengerLocation.longitude}, selectedTime);
    }

    private void handleResults(List<Ride> results)
    {
        this.results = results;
        helper.recyclerView.setAdapter(new DriverResultsAdapter(this, formHolder, results));
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        this.formHolder = formHolder;
        formHolder.setTitle(getString(R.string.passenger_pick_driver));
        query = (Query) formHolder.getDataObject(PassengerSignupActivity.QUERY);
        selectedTime = (ZonedDateTime) formHolder.getDataObject(PassengerSignupActivity.SELECTED_TIME);

        results = new ArrayList<>();
        rideResultsObserver = createListObserver(R.layout.empty_with_alert,
                rides -> {
                    handleResults(rides);
                });

        passengerLocation = (LatLng) formHolder.getDataObject(PassengerSignupActivity.LATLNG);

        formHolder.setNavigationVisibility(View.VISIBLE);
        formHolder.setNextVisibility(View.GONE);
        formHolder.setPreviousVisibility(View.VISIBLE);
        getRides();
    }
}
