package org.androidcru.crucentralcoast.data.services;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.models.Ride;

import java.util.ArrayList;

import retrofit.http.GET;
import rx.Observable;

public interface CruApiService
{
    /**
     * Method modeling the GET request for Events
     * @return Callback request
     */
    @GET("/api/event/list")
    public Observable<ArrayList<CruEvent>> getEvents();

    @GET("/api/ministry/list")
    public Observable<ArrayList<MinistrySubscription>> getMinistries();

    @GET("/api/campus/list")
    public Observable<ArrayList<Campus>> getCampuses();

    @GET("/api/ride/list")
    Observable<ArrayList<Ride>> getRides();
}
