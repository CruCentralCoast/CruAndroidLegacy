package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class EventsPresenter extends MvpBasePresenter<EventsView>
{
    public EventsPresenter()
    {
        EventBus.getDefault().register(this);
    }
    public void getEventData()
    {
        EventProvider.getEventProvider().requestEvents();
    }

    public void onEventMainThread(EventListEvent eventListEvent)
    {
        getView().setEvents(eventListEvent.eventList);
    }
}
