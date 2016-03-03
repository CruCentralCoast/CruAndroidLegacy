package org.androidcru.crucentralcoast.presentation.views.events;


import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.presentation.viewmodels.events.CruEventVM;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventsFragment extends Fragment
{
    //Injected Views
    @Bind(R.id.event_list) RecyclerView mEventList;
    @Bind(R.id.event_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.empty_events_view) RelativeLayout mEmptyEventsLayout;

    private ArrayList<CruEventVM> mCruEventVMs;
    private LinearLayoutManager mLayoutManager;

    private Observer<ArrayList<CruEvent>> mEventSubscriber;

    private SharedPreferences mSharedPreferences;

    public EventsFragment()
    {
        mCruEventVMs = new ArrayList<>();
        mEventSubscriber = new Observer<ArrayList<CruEvent>>()
        {
            @Override
            public void onCompleted()
            {
                if (mCruEventVMs.isEmpty())
                {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mEmptyEventsLayout.setVisibility(View.VISIBLE);
                } else
                {
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mEmptyEventsLayout.setVisibility(View.GONE);
                }
            }

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
     *
     * @param inflater           Object used to inflate new views, provided by Android
     * @param container          Parent view to inflate in, provided by Android
     * @param savedInstanceState State of the application if it is being refreshed, given to Android by dev
     * @return inflated View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    /**
     * Invoked after onCreateView() and deals with binding view references after the
     * view has already been inflated.
     *
     * @param view               Inflated View created by onCreateView()
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
        EventsAdapter mEventAdapter = new EventsAdapter(new ArrayList<>(), mLayoutManager);
        mEventList.setAdapter(mEventAdapter);
        mEventList.setHasFixedSize(true);

        //Set up SwipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeColors(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        mSwipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getCruEvents();
    }

    @OnClick(R.id.subscription_button)
    @SuppressWarnings("unused")
    public void onManageSubscriptionsClicked()
    {
        startActivity(new Intent(getContext(), SubscriptionActivity.class));
    }

    private void forceUpdate()
    {
        EventProvider.getInstance().requestEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mEventSubscriber);
    }

    private void getCruEvents()
    {
        EventProvider.getInstance().requestEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mEventSubscriber);
    }


    /**
     * Updates the UI to reflect the Events in events
     *
     * @param cruEvents List of new Events the UI should adhere to
     */
    public void setEvents(ArrayList<CruEvent> cruEvents)
    {
        mCruEventVMs.clear();
        rx.Observable.from(cruEvents)
                .filter(cruEvent -> {
                    for (String s : cruEvent.mParentMinistrySubscriptions)
                        if (mSharedPreferences.getBoolean(s, false))
                            return true;
                    return false;
                })
                .map(cruEvent -> {
                    return new CruEventVM(cruEvent, false,
                            mSharedPreferences.contains(cruEvent.mId),
                            mSharedPreferences.getLong(cruEvent.mId, -1));
                })
                .subscribeOn(Schedulers.immediate())
                .subscribe(mCruEventVMs::add);

        mEventList.setAdapter(new EventsAdapter(mCruEventVMs, mLayoutManager));
        mSwipeRefreshLayout.setRefreshing(false);
    }
}