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
    @Bind(R.id.event_list) RecyclerView eventList;
    @Bind(R.id.event_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_events_view) RelativeLayout emptyEventsLayout;

    private ArrayList<CruEventVM> cruEventVMs;
    private LinearLayoutManager layoutManager;
    private Observer<ArrayList<CruEvent>> eventSubscriber;
    private SharedPreferences sharedPreferences;

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

        cruEventVMs = new ArrayList<>();
        setupObserver();

        sharedPreferences = CruApplication.getSharedPreferences();

        //LayoutManager for RecyclerView
        layoutManager = new LinearLayoutManager(getActivity());
        eventList.setLayoutManager(layoutManager);

        //Adapter for RecyclerView
        EventsAdapter mEventAdapter = new EventsAdapter(new ArrayList<>(), layoutManager);
        eventList.setAdapter(mEventAdapter);
        eventList.setHasFixedSize(true);

        setupSwipeRefreshLayout();
    }

    private void setupSwipeRefreshLayout()
    {
        //issue 77712, workaround until Google fixes it
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK,View.MEASURED_HEIGHT_STATE_SHIFT);

        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
        swipeRefreshLayout.setOnRefreshListener(this::getCruEvents);
    }

    private void setupObserver()
    {
        eventSubscriber = new Observer<ArrayList<CruEvent>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);
                if (cruEventVMs.isEmpty())
                {
                    emptyEventsLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);

                }
                else
                {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    emptyEventsLayout.setVisibility(View.GONE);
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

    private void getCruEvents()
    {
        swipeRefreshLayout.setRefreshing(true);

        EventProvider.requestEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventSubscriber);
    }


    /**
     * Updates the UI to reflect the Events in events
     *
     * @param cruEvents List of new Events the UI should adhere to
     */
    public void setEvents(ArrayList<CruEvent> cruEvents)
    {
        cruEventVMs.clear();
        rx.Observable.from(cruEvents)
                .filter(cruEvent -> {
                    for (String s : cruEvent.parentMinistrySubscriptions)
                        if (sharedPreferences.getBoolean(s, false))
                            return true;
                    return false;
                })
                .map(cruEvent -> {
                    return new CruEventVM(cruEvent, false,
                            sharedPreferences.contains(cruEvent.id),
                            sharedPreferences.getLong(cruEvent.id, -1));
                })
                .subscribeOn(Schedulers.immediate())
                .subscribe(cruEventVMs::add);

        eventList.setAdapter(new EventsAdapter(cruEventVMs, layoutManager));
    }
}