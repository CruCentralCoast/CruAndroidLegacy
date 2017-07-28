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
import com.crucentralcoast.app.presentation.viewmodels.ridesharing.MyRidesDriverVM;
import com.crucentralcoast.app.presentation.views.MainActivity;
import com.crucentralcoast.app.presentation.views.base.ListFragment;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.observers.Observers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MyRidesDriverFragment extends ListFragment
{
    private ArrayList<MyRidesDriverVM> rideVMs;
    private Observer<List<Ride>> rideSubscriber;


    public MyRidesDriverFragment()
    {
        rideVMs = new ArrayList<>();

        rideSubscriber = createListObserver(R.layout.empty_my_rides_driver_view,
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
        //Due to @OnClick, this Fragment requires that the emptyView be inflated before any ButterKnife calls
        inflateEmptyView(view, R.layout.empty_my_rides_driver_view);

        super.onViewCreated(view, savedInstanceState);
        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout

        //Enables actions in the Activity Toolbar (top-right buttons)
        setHasOptionsMenu(true);

        Button redirectEvents = (Button) view.findViewById(R.id.events_button_driver);
        redirectEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToEvents();
            }
        });

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        helper.recyclerView.setLayoutManager(layoutManager);

        helper.swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    @Override
    public void onResume() {
        super.onResume();
        forceUpdate();
    }

    //TODO better security than this
    public void forceUpdate()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        RideProvider.requestMyRidesDriver(this, rideSubscriber, SharedPreferencesUtil.getFCMID());
    }

    /**
     * Updates the UI to reflect the Events in events
     * @param rides List of new Events the UI should adhere to
     */
    public void setRides(List<Ride> rides)
    {
        rideVMs.clear();
        rx.Observable.from(rides)
                .map(ride -> new MyRidesDriverVM(this, ride, false))
                .subscribeOn(Schedulers.immediate())
                .subscribe(Observers.create(vm -> rideVMs.add(vm), e -> Timber.e("Adding RideVM error", e)));

        helper.recyclerView.setAdapter(new MyRidesDriverAdapter(rideVMs, getContext()));
        helper.swipeRefreshLayout.setRefreshing(false);
    }
}
