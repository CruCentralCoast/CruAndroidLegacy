package org.androidcru.crucentralcoast.data.providers.events;

import org.androidcru.crucentralcoast.data.models.Event;

import java.util.ArrayList;

public class EventListEvent
{
    public ArrayList<Event> eventList;

    public EventListEvent(ArrayList<Event> eventList)
    {
        this.eventList = eventList;
    }
}
