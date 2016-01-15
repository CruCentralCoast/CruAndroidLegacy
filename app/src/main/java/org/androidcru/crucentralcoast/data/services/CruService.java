package org.androidcru.crucentralcoast.data.services;
import org.androidcru.crucentralcoast.data.models.CruEvent;

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
    public Call<ArrayList<CruEvent>> getEvents();

    @POST("api/event/create")
    public Call<CruEvent> postEvent(@Body CruEvent cruEvent);
}
