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
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.MyRidesDriverVM;
import org.androidcru.crucentralcoast.presentation.views.ListFragment;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyRidesDriverFragment extends ListFragment
{
    private ArrayList<MyRidesDriverVM> rideVMs;
    private Observer<List<Ride>> rideSubscriber;
    private Subscription subscription;

    public MyRidesDriverFragment()
    {
        rideVMs = new ArrayList<>();
        rideSubscriber = new Observer<List<Ride>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);

                if (rideVMs.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.GONE);
            }

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
        //Due to @OnClick, this Fragment requires that the emptyView be inflated before any ButterKnife calls
        inflateEmptyView(R.layout.empty_my_rides_driver_view);

        super.onViewCreated(view, savedInstanceState);
        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout

        //Enables actions in the Activity Toolbar (top-right buttons)
        setHasOptionsMenu(true);

        //LayoutManager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    @Override
    public void onResume() {
        super.onResume();
        forceUpdate();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(subscription != null)
            subscription.unsubscribe();
    }

    //TODO better security than this
    public void forceUpdate()
    {
        swipeRefreshLayout.setRefreshing(true);
        if(subscription != null)
            subscription.unsubscribe();
        subscription = RideProvider.requestRides()
                .flatMap(rides -> Observable.from(rides))
                .filter(ride -> ride.gcmID.equals(CruApplication.getGCMID()))
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
                .map(ride -> new MyRidesDriverVM(this, ride, false))
                .subscribeOn(Schedulers.immediate())
                .subscribe(rideVMs::add);

        recyclerView.setAdapter(new MyRidesDriverAdapter(rideVMs, getContext()));
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.events_button_driver)
    @SuppressWarnings("unused")
    public void onViewUpcomingEventsClicked()
    {
        ((MainActivity)getActivity()).switchToEvents();
    }
}
