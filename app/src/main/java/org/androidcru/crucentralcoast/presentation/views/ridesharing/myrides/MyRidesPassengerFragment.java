package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.MyRidesPassengerVM;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observer;

public class MyRidesPassengerFragment extends ListFragment
{
    private ArrayList<MyRidesPassengerVM> rideVMs;
    private Observer<List<Ride>> rideSubscriber;


    public MyRidesPassengerFragment()
    {
        rideVMs = new ArrayList<>();
        rideSubscriber = createListObserver(R.layout.empty_my_rides_passenger_view,
                rides -> setRides(rides));
    }

    /**
     * Invoked early on from the Android framework during rendering.
     * @param inflater Object used to inflate new views, provided by Android
     * @param container Parent view to inflate in, provided by Android
     * @param savedInstanceState State of the application if it is being refreshed, given to Android by dev
     * @return inflated View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    /**
     * Invoked after onCreateView() and deals with binding view references after the
     * view has already been inflated.
     * @param view Inflated View created by onCreateView()
     *
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_my_rides_passenger_view);

        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout
        super.onViewCreated(view, savedInstanceState);

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        helper.recyclerView.setLayoutManager(layoutManager);

        //Set up SwipeRefreshLayout
        helper.swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);

        forceUpdate();
    }

    public void forceUpdate()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        RideProvider.requestMyRidesPassenger(this, rideSubscriber, SharedPreferencesUtil.getGCMID());
    }

    /**
     * Updates the UI to reflect the Events in events
     * @param rides List of new Events the UI should adhere to
     */
    public void setRides(List<Ride> rides)
    {
        rideVMs.clear();
//        rx.Observable.from(rides)
//                .map(ride -> new MyRidesPassengerVM(this, ride, false))
//                .subscribeOn(Schedulers.immediate())
//                .subscribe(rideVMs::add);
        for (Ride ride : rides)
            rideVMs.add(new MyRidesPassengerVM(this, ride, false));

        helper.recyclerView.setAdapter(new MyRidesPassengerAdapter(rideVMs));
    }

    @OnClick(R.id.events_button)
    @SuppressWarnings("unused")
    public void onViewUpcomingEventsClicked()
    {
        ((MainActivity)getActivity()).switchToEvents();
    }
}
