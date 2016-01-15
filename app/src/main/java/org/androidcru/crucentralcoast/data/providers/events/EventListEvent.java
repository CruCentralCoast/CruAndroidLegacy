package org.androidcru.crucentralcoast.data.providers.events;

import org.androidcru.crucentralcoast.data.models.CruEvent;

import java.util.ArrayList;

/**
 * EventListEvent is used in EventBus for transmitting EventList from EventListProvider to
 * any subscriber in the Presentation layer.
 */
public class EventListEvent
{
    public ArrayList<CruEvent> cruEventList;

    public EventListEvent(ArrayList<CruEvent> cruEventList)
    {
        this.cruEventList = cruEventList;
    }
}
