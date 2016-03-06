package org.androidcru.crucentralcoast.presentation.views.ridesharing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.CruEventVM;
import org.androidcru.crucentralcoast.presentation.views.ListFragment;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RideSharingFragment extends ListFragment
{
    private ArrayList<CruEventVM> mCruEventVMs;
    private LinearLayoutManager mLayoutManager;

    private Observer<ArrayList<CruEvent>> mEventSubscriber;

    public RideSharingFragment()
    {
        mCruEventVMs = new ArrayList<>();
        mEventSubscriber = new Observer<ArrayList<CruEvent>>()
        {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e)
            {
                Logger.e(e, "CruEvents failed to retrieve.");
            }

            @Override
            public void onNext(ArrayList<CruEvent> cruEvents)
            {
                setEvents(cruEvents);
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this::getCruEvents);

        getCruEvents();
    }

    private void getCruEvents()
    {
        EventProvider.requestEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mEventSubscriber);
    }


    /**
     * Updates the UI to reflect the Events in events
     * @param cruEvents List of new Events the UI should adhere to
     */
    public void setEvents(ArrayList<CruEvent> cruEvents)
    {
        mCruEventVMs.clear();
        rx.Observable.from(cruEvents)
                .filter(cruEvent1 -> cruEvent1.rideSharingEnabled)
                .map(cruEvent -> new CruEventVM(cruEvent, false, getActivity()))
                .subscribeOn(Schedulers.immediate())
                .subscribe(mCruEventVMs::add);

        recyclerView.setAdapter(new RideSharingAdapter(mCruEventVMs, mLayoutManager));
        swipeRefreshLayout.setRefreshing(false);
    }
}
