package org.androidcru.crucentralcoast.presentation.views.views;

import org.androidcru.crucentralcoast.data.models.Event;

import java.util.ArrayList;

public interface EventsView extends MvpView
{
    //methods for the Presenter to interface with the EventsFragment
    public void setEvents(ArrayList<Event> events);
}
