package org.androidcru.crucentralcoast.data.services;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.models.Ride;

import java.util.ArrayList;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
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

    @GET("/api/ministryteam/list")
    public Observable<ArrayList<MinistryTeam>> getMinistryTeams();

    @FormUrlEncoded
    @POST("/api/user/find")
    public Observable<ArrayList<CruUser>> getMinistryTeamLeaders(@Field("ministryTeams") ArrayList<String> ministryId);

    @GET("/api/ride/list")
    Observable<ArrayList<Ride>> getRides();

    //@POST("/api/passenger/create")
    //Observable<Passenger> postPassenger(@Body Passenger passenger);


}
