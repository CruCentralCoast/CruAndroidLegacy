package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.presentation.presenters.EventsPresenter;
import org.androidcru.crucentralcoast.presentation.views.adapters.EventsAdapter;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventsFragment extends MvpFragment<EventsPresenter> implements EventsView
{
    //Injected Views
    @Bind(R.id.event_list) RecyclerView mEventList;

    //View elements
    LinearLayoutManager mLayoutManager;
    EventsAdapter mEventAdapter;

    /**
     * When the framework invokes onCreateView(), MvpFragment will invoke this method and create the
     * corresponding presenter.
     * @return EventPresenter
     */
    @Override
    protected EventsPresenter createPresenter()
    {
        return new EventsPresenter();
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

        //Enables actions in the Activity Toolbar (top-right buttons)
        setHasOptionsMenu(true);

        //LayoutManager for RecyclerView
        mLayoutManager = new LinearLayoutManager(getActivity());
        mEventList.setLayoutManager(mLayoutManager);

        //Adapter for RecyclerView
        mEventAdapter = new EventsAdapter(new ArrayList<Event>(), mLayoutManager);
        mEventList.setAdapter(mEventAdapter);
        mEventList.setHasFixedSize(true);

        //Ask presenter for data
        presenter.getEventData();


    }

    /**
     * Inovoked by the Android framework if setHasOptionsMenu() is called
     * @param menu Reference to Menu, provided by Android
     * @param inflater Inflater object, provided by Android
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_events, menu);
    }

    /**
     * Click listener for when actions in the Toolbar are clicked
     * @param item Item clicked, provided by Android
     * @return True to consume the touch event or false to allow Android to handle it
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch(itemId)
        {
            case R.id.action_add_event:
                presenter.postRandomEvent();
                return true;
            case R.id.action_refresh:
                presenter.refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates the UI to reflect the Events in events
     * @param events List of new Events the UI should adhere to
     */
    @Override
    public void setEvents(ArrayList<Event> events)
    {
        mEventList.setAdapter(new EventsAdapter(events, mLayoutManager));
    }
}
