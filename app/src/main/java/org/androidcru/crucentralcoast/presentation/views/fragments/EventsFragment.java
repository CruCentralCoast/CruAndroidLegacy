package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    @Bind(R.id.event_list)
    RecyclerView mEventList;

    LinearLayoutManager mLayoutManager;
    EventsAdapter mEventAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mEventList.setLayoutManager(mLayoutManager);
        presenter.getEventData();
        // specify an adapter (see also next example)
        mEventAdapter = new EventsAdapter(new ArrayList<Event>());
        mEventList.setHasFixedSize(false);
        //mEventList.setNestedScrollingEnabled(false);
        //mEventList.setNestedScrollingEnabled(false);
        mEventList.setAdapter(mEventAdapter);
    }

    @Override
    protected EventsPresenter createPresenter()
    {
        return new EventsPresenter();
    }


    @Override
    public void setEvents(ArrayList<Event> events)
    {
        mEventList.setAdapter(new EventsAdapter(events));
    }
}
