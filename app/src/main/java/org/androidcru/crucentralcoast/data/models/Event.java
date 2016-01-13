package org.androidcru.crucentralcoast.data.models;

import org.threeten.bp.ZonedDateTime;

public class Event
{
    public String name;
    public String description;
    public ZonedDateTime startDate;
    public ZonedDateTime endDate;
    public boolean rideSharingEnabled;
    public Location location;

    /**
     * Required by GSON/RetroFit in order to automatically create and populate via reflection
     */
    public Event() {}

    public Event(String name, String description, ZonedDateTime startDate, ZonedDateTime endDate, Location location, boolean rideSharingEnabled)
    {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.rideSharingEnabled = rideSharingEnabled;
    }
}
