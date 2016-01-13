package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.providers.EventProvider;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;

public class EventsPresenter extends MvpBasePresenter<EventsView>
{
    /**
     * Invoked by MvpFragment (or MvpActivity) when their view is inflated
     * @param view Select methods allowed to execute on the view
     */
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

    /**
     * Invoked by EventBus on the main UI thread. UI thread required since the body of the method
     * involves UI code
     * @param eventListEvent Unique event that contains the new EventList the UI should adhere to
     */
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
