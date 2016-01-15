package org.androidcru.crucentralcoast.presentation.views.interactors;

import org.androidcru.crucentralcoast.data.models.CruEvent;

import java.util.ArrayList;

public interface EventsInteractor extends MvpInteractor
{
    /**
     * Updates the UI to reflect the Events in events
     * @param cruEvents List of new Events the UI should adhere to
     */
    public void setEvents(ArrayList<CruEvent> cruEvents);
}
