package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.MyRidesPassengerVM;
import org.androidcru.crucentralcoast.presentation.views.ListFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyRidesPassengerFragment extends ListFragment
{
    private ArrayList<MyRidesPassengerVM> rideVMs;
    private Observer<List<Ride>> rideSubscriber;

    public MyRidesPassengerFragment()
    {
        rideVMs = new ArrayList<>();
        rideSubscriber = new Observer<List<Ride>>()
        {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e)
            {
                Logger.e(e, "Rides failed to retrieve.");
            }

            @Override
            public void onNext(List<Ride> rides)
            {
                setRides(rides);
            }
        };
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
     * @param savedInstanceState State of the application if it is being refreshed, given to Android by dev
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);

        forceUpdate();
    }



    private void forceUpdate()
    {
        RideProvider.requestRides()
                .flatMap(rides -> Observable.from(rides))
                .filter(ride -> {
                    for (Passenger p : ride.passengers)
                    {
                        if (p.gcm_id != null && p.gcm_id.equals(CruApplication.getGCMID()))
                        {
                            return true;
                        }
                    }
                    return false;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(rideSubscriber);
    }

    /**
     * Updates the UI to reflect the Events in events
     * @param rides List of new Events the UI should adhere to
     */
    public void setRides(List<Ride> rides)
    {
        rideVMs.clear();
        rx.Observable.from(rides)
                .map(ride -> new MyRidesPassengerVM(ride, false, getActivity()))
                .subscribeOn(Schedulers.immediate())
                .subscribe(rideVMs::add);
        recyclerView.setAdapter(new MyRidesPassengerAdapter(rideVMs));
        swipeRefreshLayout.setRefreshing(false);
    }
}
