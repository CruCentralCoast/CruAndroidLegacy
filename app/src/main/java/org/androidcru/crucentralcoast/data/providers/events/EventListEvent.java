package org.androidcru.crucentralcoast.data.providers.events;

import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.data.models.EventList;

import java.util.ArrayList;

public class EventListEvent
{
    public ArrayList<Event> eventList;

    public EventListEvent(ArrayList<Event> eventList)
    {
        this.eventList = eventList;
    }
}
