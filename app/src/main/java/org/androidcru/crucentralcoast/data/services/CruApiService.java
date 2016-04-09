package org.androidcru.crucentralcoast.data.services;

import com.google.gson.JsonObject;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.androidcru.crucentralcoast.data.models.queries.Query;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface CruApiService
{
    @GET("/api/users/phone/{phone}") // ask jon
    public Observable<CruUser> getCruUser(@Path("phone") String phoneNumber);

    @GET("/api/events/") // check
    public Observable<ArrayList<CruEvent>> getEvents();

    @GET("/api/ministries/") // check
    public Observable<ArrayList<MinistrySubscription>> getMinistries();

    @GET("/api/campuses/") // check
    public Observable<ArrayList<Campus>> getCampuses();

    @GET("/api/ministryteams/") // check
    public Observable<ArrayList<MinistryTeam>> getMinistryTeams();

    @FormUrlEncoded
    @POST("/api/users/find") // check
    public Observable<ArrayList<CruUser>> getMinistryTeamLeaders(@Field("ministryTeams") ArrayList<String> ministryId);

    @GET("/api/rides/") // check
    Observable<ArrayList<Ride>> getRides();

    @POST("/api/rides/search") // check
    Observable<ArrayList<Ride>> searchRides(@Body Query query);

    @POST("api/resources/find")
    Observable<ArrayList<Resource>> findResources(@Body JsonObject query);

    @POST("/api/resourcetags/find")
    Observable<ArrayList<ResourceTag>> findResourceTag(@Body JsonObject query);

    @GET("/api/resourcetags/")
    Observable<ArrayList<ResourceTag>> getResourceTag();

    @FormUrlEncoded
    @POST("/api/rides/find")
    Observable<ArrayList<Ride>> findSingleRide(@Field("_id") String id);

    @FormUrlEncoded
    @POST("/api/passengers/find")
    Observable<ArrayList<Passenger>> findSinglePassenger(@Field("_id") String id);

    @FormUrlEncoded
    @POST("/api/events/find")
    Observable<ArrayList<CruEvent>> findSingleCruEvent(@Field("_id") String id);

    @POST("/api/rides") // need to update model for this
    Observable<Ride> postRide(@Body Ride ride);

    //@POST("/api/rides/update")
    @POST("/api/rides/")
    Observable<Ride> updateRide(@Body Ride ride);

    @POST("/api/passengers")
    Observable<Passenger> createPassenger(@Body Passenger passenger);

    @FormUrlEncoded
    @POST("/api/rides/{ride_id}/passengers")
    Observable<Void> addPassenger(@Path("ride_id") String rideId, @Field("passenger_id") String passengerId);

    @DELETE("/api/rides/{ride_id}/passengers/{passenger_id}")
    Observable<Void> dropPassenger(@Path("ride_id") String rideId, @Path("passenger_id") String passengerId);

    @FormUrlEncoded
    @DELETE("/api/rides")
    Observable<Void> dropRide(@Field("ride_id") String rideId);

    @GET("/api/summermissions/")
    Observable<ArrayList<SummerMission>> getSummerMissions();

}
