package org.androidcru.crucentralcoast.data.providers;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.data.models.EventList;
import org.androidcru.crucentralcoast.data.providers.events.EventListEvent;
import org.androidcru.crucentralcoast.data.services.CruService;
import org.androidcru.crucentralcoast.presentation.views.activities.MainActivity;

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

    private EventProvider() {}

    public static EventProvider getEventProvider()
    {
        if (eventProvider == null)
            eventProvider = new EventProvider();
        return eventProvider;
    }

    public void requestEvents()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .build();

        CruService cruService = retrofit.create(CruService.class);

        Call<ArrayList<Event>> getEvents = cruService.getEvents();

        getEvents.enqueue(new Callback<ArrayList<Event>>()
        {
            @Override
            public void onResponse(Response<ArrayList<Event>> response, Retrofit retrofit)
            {
                EventBus.getDefault().post(new EventListEvent(response.body()));
            }

            @Override
            public void onFailure(Throwable t)
            {
                Logger.e(t, "Retrofit failed to get the event.");
            }
        });
    }
}
