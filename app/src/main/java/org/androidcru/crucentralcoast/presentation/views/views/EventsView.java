package org.androidcru.crucentralcoast.presentation.views.views;

import org.androidcru.crucentralcoast.data.models.Event;

import java.util.ArrayList;

public interface EventsView extends MvpView
{
    /**
     * Updates the UI to reflect the Events in events
     * @param events List of new Events the UI should adhere to
     */
    public void setEvents(ArrayList<Event> events);
}
