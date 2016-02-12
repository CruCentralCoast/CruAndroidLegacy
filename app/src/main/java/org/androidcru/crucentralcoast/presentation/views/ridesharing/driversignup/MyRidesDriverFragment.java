package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.views.adapters.MyRidesDriverAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyRidesDriverFragment extends Fragment
{
    //Injected Views
    @Bind(R.id.event_list) RecyclerView mEventList;
    @Bind(R.id.event_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<Ride> rides;
    private LinearLayoutManager mLayoutManager;

    private Observer<ArrayList<Ride>> mRideSubscriber;

    private SharedPreferences mSharedPreferences;

    public MyRidesDriverFragment()
    {
        rides = new ArrayList<Ride>();
        mRideSubscriber = new Observer<ArrayList<Ride>>()
        {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e)
            {
                Logger.e(e, "CruEvents failed to retrieve.");
            }

            @Override
            public void onNext(ArrayList<Ride> rides)
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
    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_events, container, false);
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

        //Let ButterKnife find all injected views and bind them to member variables
        ButterKnife.bind(this, view);

        mSharedPreferences = CruApplication.getSharedPreferences();

        //Enables actions in the Activity Toolbar (top-right buttons)
        setHasOptionsMenu(true);

        //LayoutManager for RecyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mEventList.setLayoutManager(mLayoutManager);

        //Adapter for RecyclerView
        MyRidesDriverAdapter mRideSharingAdapter = new MyRidesDriverAdapter(getActivity(), new ArrayList<>(), mLayoutManager);
        mEventList.setAdapter(mRideSharingAdapter);
        mEventList.setHasFixedSize(true);

        //Set up SwipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeColors(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        mSwipeRefreshLayout.setOnRefreshListener(this::forceUpdate);

        getCruEvents();
    }

    private void forceUpdate()
    {
        RideProvider.getInstance().requestRides()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRideSubscriber);
    }

    private void getCruEvents()
    {
        RideProvider.getInstance().requestRides()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRideSubscriber);
    }

    /**
     * Updates the UI to reflect the Events in events
     * @param newRides List of new Events the UI should adhere to
     */
    public void setRides(ArrayList<Ride> newRides)
    {
        rides.clear();
        rx.Observable.from(newRides)
                .map(ride -> new Ride(ride.mDriverName, ride.mDriverNumber, ride.mGender, ride.mEventId, ride.mTime, ride.mLocation, ride.mPassengers))
                .subscribeOn(Schedulers.immediate())
                .subscribe(rides::add);

        mEventList.setAdapter(new MyRidesDriverAdapter(getActivity(), rides, mLayoutManager));
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
