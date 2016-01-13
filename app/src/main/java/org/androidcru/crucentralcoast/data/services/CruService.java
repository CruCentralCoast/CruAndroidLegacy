package org.androidcru.crucentralcoast.data.services;
import org.androidcru.crucentralcoast.data.models.Event;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface CruService
{
    /**
     * Method modeling the GET request for Events
     * @return Callback request
     */
    @GET("/api/event/list")
    public Call<ArrayList<Event>> getEvents();

    @POST("api/event/create")
    public Call<Event> postEvent(@Body Event event);
}
