package org.androidcru.crucentralcoast.data.services;

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
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface CruApiService
{
    @GET("/api/users/phone/{phone}") // ask jon
    public Observable<CruUser> getCruUser(@Path("phone") String phoneNumber);

    @GET("/api/events/")
    public Observable<ArrayList<CruEvent>> getEvents();

    @POST("/api/events/search")
    Observable<List<CruEvent>> searchEvents(@Body Query query);

    @GET("/api/ministries/")
    public Observable<ArrayList<MinistrySubscription>> getMinistries();

    @GET("/api/campuses/")
    public Observable<ArrayList<Campus>> getCampuses();

    @GET("/api/ministryteams/")
    public Observable<ArrayList<MinistryTeam>> getMinistryTeams();

    @POST("/api/ministryteams/{id}/join")
    public Observable<Void> joinMinistryTeam(@Path("id") String id, @Body CruUser user);

    @FormUrlEncoded
    @POST("/api/users/find")
    public Observable<ArrayList<CruUser>> getMinistryTeamLeaders(@Field("ministryTeams") ArrayList<String> ministryId);

    @GET("/api/rides/")
    Observable<ArrayList<Ride>> getRides();

    @POST("/api/rides/search")
    Observable<ArrayList<Ride>> searchRides(@Body Query query);

    @POST("api/resources/search")
    Observable<ArrayList<Resource>> findResources(@Body Query query);

    @GET("api/resources")
    Observable<ArrayList<Resource>> getResources();

    @POST("/api/resourcetags/search")
    Observable<ArrayList<ResourceTag>> findResourceTag(@Body Query query);

    @GET("/api/resourcetags")
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

    @POST("/api/rides")
    Observable<Ride> postRide(@Body Ride ride);

    @PATCH("/api/rides/{ride_id}")
    Observable<Ride> updateRide(@Path("ride_id") String rideId, @Body Ride ride);

    @POST("/api/passengers")
    Observable<Passenger> createPassenger(@Body Passenger passenger);

    @FormUrlEncoded
    @POST("/api/rides/{ride_id}/passengers")
    Observable<Void> addPassenger(@Path("ride_id") String rideId, @Field("passenger_id") String passengerId);

    @DELETE("/api/rides/{ride_id}/passengers/{passenger_id}")
    Observable<Void> dropPassenger(@Path("ride_id") String rideId, @Path("passenger_id") String passengerId);

    @DELETE("/api/rides/{ride_id}")
    Observable<Void> dropRide(@Path("ride_id") String rideId);

    @GET("/api/summermissions/")
    Observable<ArrayList<SummerMission>> getSummerMissions();

}
