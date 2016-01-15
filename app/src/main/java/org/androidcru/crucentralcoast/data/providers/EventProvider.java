package org.androidcru.crucentralcoast.data.providers;

import com.anupcowkur.reservoir.Reservoir;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.CruEventsList;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.data.services.CruService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * EventProvider is a Singleton class designed to provide Presenters with a static reference to
 * the EventList object.
 */
public final class EventProvider
{
    private static EventProvider eventProvider;

    private Retrofit retrofit;
    private CruService cruService;
    private EventCacheProvider cacheProvider;
    private int testCounter = 0;

    private EventProvider()
    {
        //Configures RetroFit
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .build();
        //Generates concrete implementation of CruService
        cruService = retrofit.create(CruService.class);
    }

    public static EventProvider getInstance()
    {
        if (eventProvider == null)
            eventProvider = new EventProvider();
        return eventProvider;
    }

    /**
     * Posts an EventListEvent onto the EventBus if EventList is cached or otherwise, a cold request
     * is issued.
     */
    public void requestEvents()
    {
        if(cacheProvider == null)
        {
            cacheProvider = new EventCacheProvider();
        }
        CruEventsList events = cacheProvider.checkCache();
        if(events == null)
        {
            forceUpdate();
        }
        else
        {
            Logger.d("Reservoir not empty! Loading from cache...");
            EventBus.getDefault().post(new EventListEvent(events.cruEvents));
        }
    }

    /**
     * Invalidates the cache and issues a cold request for a new EventList via the network.
     */
    public void forceUpdate()
    {
        //Generates a queue for EventList related network requests
        Call<ArrayList<CruEvent>> getEvents = cruService.getEvents();

        //Adds a network events to the queue and asynchronously issues the request
        getEvents.enqueue(new Callback<ArrayList<CruEvent>>()
        {
            /**
             * Called if the response succeeded
             * @param response Parsed POJO of the JSON response
             * @param retrofit Retrofit object
             */
            @Override
            public void onResponse(Response<ArrayList<CruEvent>> response, Retrofit retrofit)
            {
                Logger.d("Reservoir being refilled! Writing to cache...");
                cacheProvider.putCache(new CruEventsList(response.body()));
                EventBus.getDefault().post(new EventListEvent(response.body()));
            }

            /**
             * Called if the response failed
             * @param t Exception that was thrown
             */
            @Override
            public void onFailure(Throwable t)
            {
                Logger.e(t, "Retrofit failed to get the event.");
            }
        });
    }

    /**
     * EventCacheProvider handles caching the EventList to disk
     */
    private class EventCacheProvider
    {
        //key used to retrieve the EventList from disk
        private final String eventKey = "event_key";

        /**
         * Asks the disk cache if it has a copy of EventList
         * @return EventList if if the cache has an entry, null otherwise
         */
        public CruEventsList checkCache()
        {
            CruEventsList events = null;
            try
            {
                if(Reservoir.contains(eventKey))
                {
                    events = Reservoir.get(eventKey, CruEventsList.class);
                }

            }
            catch(Exception e)
            {
                Logger.e(e, "Reservoir error!");
            }
            return events;
        }

        /**
         * Invalidates cache and places a new EventList into cache
         * @param events EventList to place into cache
         */
        public void putCache(CruEventsList events)
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
