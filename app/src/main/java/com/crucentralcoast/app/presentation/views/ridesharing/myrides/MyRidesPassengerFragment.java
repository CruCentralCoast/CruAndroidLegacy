package com.crucentralcoast.app.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.presentation.viewmodels.ridesharing.MyRidesPassengerVM;
import com.crucentralcoast.app.presentation.views.MainActivity;
import com.crucentralcoast.app.presentation.views.base.ListFragment;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class MyRidesPassengerFragment extends ListFragment {
    private ArrayList<MyRidesPassengerVM> rideVMs;
    private Observer<List<Ride>> rideSubscriber;


    public MyRidesPassengerFragment() {
        rideVMs = new ArrayList<>();
        rideSubscriber = createListObserver(R.layout.empty_my_rides_passenger_view, this::setRides);
    }

    /**
     * Invoked early on from the Android framework during rendering.
     *
     * @param inflater           Object used to inflate new views, provided by Android
     * @param container          Parent view to inflate in, provided by Android
     * @param savedInstanceState State of the application if it is being refreshed, given to Android by dev
     * @return inflated View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        forceUpdate();
    }

    /**
     * Invoked after onCreateView() and deals with binding view references after the
     * view has already been inflated.
     *
     * @param view Inflated View created by onCreateView()
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        inflateEmptyView(view, R.layout.empty_my_rides_passenger_view);

        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout
        super.onViewCreated(view, savedInstanceState);

        Button redirectEvents = (Button) view.findViewById(R.id.events_button);
        redirectEvents.setOnClickListener(v -> ((MainActivity) getActivity()).switchToEvents());

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        helper.recyclerView.setLayoutManager(layoutManager);

        //Set up SwipeRefreshLayout
        helper.swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);

        forceUpdate();
    }

    public void forceUpdate() {
        helper.swipeRefreshLayout.setRefreshing(true);
        RideProvider.requestMyRidesPassenger(this, rideSubscriber, SharedPreferencesUtil.getFCMID());
    }

    /**
     * Updates the UI to reflect the Events in events
     *
     * @param rides List of new Events the UI should adhere to
     */
    public void setRides(List<Ride> rides) {
        rideVMs.clear();
        for (Ride ride : rides)
            rideVMs.add(new MyRidesPassengerVM(this, ride, false));

        helper.recyclerView.setAdapter(new MyRidesPassengerAdapter(rideVMs));
    }
}
