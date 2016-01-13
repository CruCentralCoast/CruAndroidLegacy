package org.androidcru.crucentralcoast.data.providers.events;

import org.androidcru.crucentralcoast.data.models.Event;

import java.util.ArrayList;

/**
 * EventListEvent is used in EventBus for transmitting EventList from EventListProvider to
 * any subscriber in the Presentation layer.
 */
public class EventListEvent
{
    public ArrayList<Event> eventList;

    public EventListEvent(ArrayList<Event> eventList)
    {
        this.eventList = eventList;
    }
}
