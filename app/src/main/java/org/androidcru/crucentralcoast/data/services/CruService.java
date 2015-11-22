package org.androidcru.crucentralcoast.data.services;
import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.data.models.EventList;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;

public interface CruService
{
    @GET("/api/event/list")
    public Call<ArrayList<Event>> getEvents();
}
