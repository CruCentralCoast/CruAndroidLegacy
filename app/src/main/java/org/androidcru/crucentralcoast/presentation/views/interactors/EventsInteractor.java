package org.androidcru.crucentralcoast.presentation.views.interactors;

import org.androidcru.crucentralcoast.data.models.Event;

import java.util.ArrayList;

public interface EventsInteractor extends MvpView
{
    /**
     * Updates the UI to reflect the Events in events
     * @param events List of new Events the UI should adhere to
     */
    public void setEvents(ArrayList<Event> events);
}
