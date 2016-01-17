package org.androidcru.crucentralcoast.data.services;

import org.androidcru.crucentralcoast.data.models.CruEvent;

import java.util.ArrayList;

import retrofit.http.GET;
import rx.Observable;

public interface CruService
{
    /**
     * Method modeling the GET request for Events
     * @return Callback request
     */
    @GET("/api/event/list")
    public Observable<ArrayList<CruEvent>> getEvents();
}
