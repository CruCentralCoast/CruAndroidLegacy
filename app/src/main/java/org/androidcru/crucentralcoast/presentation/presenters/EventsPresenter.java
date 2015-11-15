package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;
import org.threeten.bp.ZonedDateTime;

import java.net.URL;
import java.util.ArrayList;

public class EventsPresenter extends MvpBasePresenter<EventsView>
{

    public ArrayList<Event> getEventData()
    {
        ArrayList<Event> list = new ArrayList<>();
        list.add(new Event("Test Event", "", ZonedDateTime.now(), ZonedDateTime.now().plusHours(3), false));
        return list;
    }
}
