package org.androidcru.crucentralcoast.data.providers;

import com.anupcowkur.reservoir.Reservoir;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.data.models.EventList;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.data.services.CruService;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public final class EventProvider
{
    private static EventProvider eventProvider;

    private Retrofit retrofit;
    private CruService cruService;
    private EventCacheProvider cacheProvider;
    private int testCounter = 0;

    private EventProvider()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .build();

        cruService = retrofit.create(CruService.class);
    }

    public static EventProvider getInstance()
    {
        if (eventProvider == null)
            eventProvider = new EventProvider();
        return eventProvider;
    }

    public void postRandomEvent()
    {
        ZonedDateTime nowish = ZonedDateTime.now().withNano(0).withSecond(0).withMinute(0);
        Call<Event> call = cruService.postEvent(new Event("Test Event " + String.valueOf(++testCounter), "Test Description " + String.valueOf(testCounter),
                nowish, nowish.plusHours(3),
                new Location("TBD", "TBD", "TBD","TBD", "USA"), false));
        call.enqueue(new Callback<Event>()
        {
            @Override
            public void onResponse(Response<Event> response, Retrofit retrofit)
            {
                forceUpdate();
            }

            @Override
            public void onFailure(Throwable t)
            {
                Logger.e(t.getMessage());
            }
        });
    }

    public void requestEvents()
    {
        if(cacheProvider == null)
        {
            cacheProvider = new EventCacheProvider();
        }
        EventList events = cacheProvider.checkCache();
        if(events == null)
        {
            forceUpdate();
        }
        else
        {
            Logger.d("Reservoir not empty! Loading from cache...");
            EventBus.getDefault().post(new EventListEvent(events.events));
        }
    }

    public void forceUpdate()
    {
        Call<ArrayList<Event>> getEvents = cruService.getEvents();

        getEvents.enqueue(new Callback<ArrayList<Event>>()
        {
            @Override
            public void onResponse(Response<ArrayList<Event>> response, Retrofit retrofit)
            {
                Logger.d("Reservoir being refilled! Writing to cache...");
                cacheProvider.putCache(new EventList(response.body()));
                EventBus.getDefault().post(new EventListEvent(response.body()));
            }

            @Override
            public void onFailure(Throwable t)
            {
                Logger.e(t, "Retrofit failed to get the event.");
            }
        });
    }

    private class EventCacheProvider
    {
        private final String eventKey = "event_key";

        public EventList checkCache()
        {
            EventList events = null;
            try
            {
                if(Reservoir.contains(eventKey))
                {
                    events = Reservoir.get(eventKey, EventList.class);
                }

            }
            catch(Exception e)
            {
                Logger.e(e, "Reservoir error!");
            }
            return events;
        }

        public void putCache(EventList events)
        {
            try
            {
                Reservoir.put(eventKey, events);
                if(!Reservoir.contains(eventKey))
                {
                    Logger.e("Cache write failed.");
                }
            }
            catch(Exception e)
            {
                Logger.e(e, "Reservoir error!");
            }
        }
    }
}
