package org.androidcru.crucentralcoast.data.models;

import java.util.ArrayList;

/**
 * Required for disk caching
 */
public class EventList
{
    public ArrayList<Event> events;

    public EventList() {};

    public EventList(ArrayList<Event> events)
    {
        this.events = events;
    }
}
