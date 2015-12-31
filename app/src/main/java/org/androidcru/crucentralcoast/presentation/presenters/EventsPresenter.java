package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;

public class EventsPresenter extends MvpBasePresenter<EventsView>
{
    @Override
    public void onAttachView(EventsView view)
    {
        super.onAttachView(view);
        eventBus.register(this);
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

    public void refresh()
    {
        EventProvider.getInstance().forceUpdate();
    }
}
