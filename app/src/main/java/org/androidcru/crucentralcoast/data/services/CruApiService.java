package org.androidcru.crucentralcoast.data.services;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Ride;

import java.util.ArrayList;

import retrofit.http.Body;
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

    @FormUrlEncoded
    @POST("/api/ride/find")
    Observable<ArrayList<Ride>> findSingleRide(@Field("_id") String id);

    @FormUrlEncoded
    @POST("/api/passenger/find")
    Observable<ArrayList<Passenger>> findSinglePassenger(@Field("_id") String id);

    @FormUrlEncoded
    @POST("/api/event/find")
    Observable<ArrayList<CruEvent>> findSingleCruEvent(@Field("_id") String id);

    @POST("/api/ride/create")
    Observable<Ride> postRide(@Body Ride ride);

    @POST("/api/ride/update")
    Observable<Ride> updateRide(@Body Ride ride);

    @POST("/api/passenger/create")
    Observable<Passenger> createPassenger(@Body Passenger passenger);

    @FormUrlEncoded
    @POST("/api/ride/addPassenger")
    Observable<Void> addPassenger(@Field("ride_id") String rideId, @Field("passenger_id") String passengerId);

    @FormUrlEncoded
    @POST("/api/ride/dropPassenger")
    Observable<Void> dropPassenger(@Field("ride_id") String rideId, @Field("passenger_id") String passengerId);

    @FormUrlEncoded
    @POST("/api/ride/dropRide")
    Observable<Void> dropRide(@Field("ride_id") String rideId);

//    @GET("api/event/:id")
//    public CruEvent getEventByID(String id);

    //@POST("/api/passenger/create")
    //Observable<Passenger> postPassenger(@Body Passenger passenger);
}
