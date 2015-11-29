package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;

import de.greenrobot.event.EventBus;

public class EventsPresenter extends MvpBasePresenter<EventsView>
{
    public EventsPresenter()
    {
        EventBus.getDefault().register(this);
    }
    public void getEventData()
    {
        EventProvider.getInstance().requestEvents();
    }

    public void onEventMainThread(EventListEvent eventListEvent)
    {
        getView().setEvents(eventListEvent.eventList);
    }

    public void postRandomEvent()
    {
        EventProvider.getInstance().postRandomEvent();
    }
}
