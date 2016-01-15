package org.androidcru.crucentralcoast.data.models;

import java.util.ArrayList;

/**
 * Required for disk caching
 */
public class CruEventsList
{
    public ArrayList<CruEvent> mCruEvents;

    /**
     * Required by GSON/RetroFit in order to automatically create and populate via reflection
     */
    public CruEventsList() {}

    public CruEventsList(ArrayList<CruEvent> cruEvents)
    {
        this.mCruEvents = cruEvents;
    }
}
