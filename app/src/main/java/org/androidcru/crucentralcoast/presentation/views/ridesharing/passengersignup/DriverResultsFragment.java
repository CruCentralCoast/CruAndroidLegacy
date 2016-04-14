package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.google.android.gms.maps.model.LatLng;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;

public class DriverResultsFragment extends FormContentFragment
{
    @Bind(R.id.recyclerview) protected RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout) protected SwipeRefreshLayout swipeRefreshLayout;
    private View emptyView;

    private Query query;
    private List<Ride> results;
    private LatLng passengerLocation;

    private Subscription subscription;
    private Observer<List<Ride>> rideResultsObserver;

    public DriverResultsFragment()
    {
        results = new ArrayList<>();
        rideResultsObserver = new Observer<List<Ride>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);
                if (results.isEmpty())
                {
                    emptyView.setVisibility(View.VISIBLE);
                }
                else
                {
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<Ride> rides)
            {
                handleResults(rides);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ViewStub emptyViewStub = (ViewStub) view.findViewById(R.id.empty_view_stub);
        emptyViewStub.setLayoutResource(R.layout.empty_driver_results);
        emptyView = emptyViewStub.inflate();

        ButterKnife.bind(this, view);

        ListFragment.setupSwipeRefreshLayout(swipeRefreshLayout);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        formHolder.setTitle(getString(R.string.passenger_pick_driver));
        query = (Query) formHolder.getDataObject(PassengerSignupActivity.QUERY);
        passengerLocation = (LatLng) formHolder.getDataObject(PassengerSignupActivity.LATLNG);

        formHolder.setNavigationVisibility(View.VISIBLE);
        formHolder.setNextVisibility(View.GONE);
        formHolder.setPreviousVisibility(View.VISIBLE);
        getRides();
    }


    private void getRides()
    {
        swipeRefreshLayout.setRefreshing(true);
        results.clear();
        RideProvider.searchRides(this, rideResultsObserver, query,
                new Double[]{passengerLocation.latitude, passengerLocation.longitude});
    }

    private void handleResults(List<Ride> results)
    {
        this.results = results;
        recyclerView.setAdapter(new DriverResultsAdapter(this, formHolder, results));
    }
}
