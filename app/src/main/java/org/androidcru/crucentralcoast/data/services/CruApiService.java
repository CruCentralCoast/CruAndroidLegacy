package org.androidcru.crucentralcoast.data.services;

import org.androidcru.crucentralcoast.data.models.Campus;
import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.data.models.CommunityGroupRequest;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.LoginResponse;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.RideCheckResponse;
import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.androidcru.crucentralcoast.data.models.queries.Query;

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
    @GET("/api/users/phone/{phone}")
    Observable<CruUser> getCruUser(@Path("phone") String phoneNumber);

    @GET("/api/events/")
    Observable<List<CruEvent>> getEvents();

    @POST("/api/events/search")
    Observable<List<CruEvent>> searchEvents(@Body Query query);

    @GET("/api/ministries/")
    Observable<List<MinistrySubscription>> getMinistries();

    @GET("/api/campuses/")
    Observable<List<Campus>> getCampuses();

    @GET("/api/ministryteams/")
    Observable<List<MinistryTeam>> getMinistryTeams();

    @POST("/api/ministryteams/{id}/join")
    Observable<Void> joinMinistryTeam(@Path("id") String id, @Body CruUser user);

    @POST("/api/communitygroups/{id}/join")
    Observable<Void> joinCommunityGroup(@Path("id") String id, @Body CruUser user);

    @GET("/api/ministryquestions/")
    Observable<List<MinistryQuestion>> getMinistryQuestions();

    @POST("/api/users/search")
    Observable<List<CruUser>> getMinistryTeamLeaders(@Body Query query);

    @GET("/api/rides/")
    Observable<List<Ride>> getRides();

    @POST("/api/rides/search")
    Observable<List<Ride>> searchRides(@Body Query query);

    @POST("api/resources/search")
    Observable<List<Resource>> findResources(@Body Query query, @retrofit2.http.Query("LeaderAPIKey") String leaderAPIKey);

    @GET("api/resources")
    Observable<List<Resource>> getResources();

    @POST("/api/resourcetags/search")
    Observable<List<ResourceTag>> findResourceTag(@Body Query query);

    @GET("/api/resourcetags")
    Observable<List<ResourceTag>> getResourceTag();

    @POST("/api/passengers/search")
    Observable<List<Passenger>> searchPassengers(@Body Query query);

    @FormUrlEncoded
    @POST("/api/rides/find")
    Observable<List<Ride>> findSingleRide(@Field("_id") String id);

    @FormUrlEncoded
    @POST("/api/passengers/find")
    Observable<List<Passenger>> findSinglePassenger(@Field("_id") String id);

    @FormUrlEncoded
    @POST("/api/events/find")
    Observable<List<CruEvent>> findSingleCruEvent(@Field("_id") String id);

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
    Observable<List<SummerMission>> getSummerMissions();

    @FormUrlEncoded
    @POST("/api/signin")
    Observable<LoginResponse> signin(@Field("username") String user,
                                     @Field("password") String password,
                                     @Field("gcmId") String gcmId);

    @GET("/api/signout")
    Observable<LoginResponse> signout();

    @GET("/api/events/{event_id}/{gcm_id}")
    Observable<RideCheckResponse> checkRideStatus(@Path("event_id") String eventId, @Path("gcm_id") String gcmId);

    @POST("/api/Ministries/{id}/communitygroups")
    Observable<List<CommunityGroup>> getCommunityGroups(@Path("id") String ministryId, @Body CommunityGroupRequest questionAnswers);

    @FormUrlEncoded
    @POST("/api/gcm")
    Observable<Void> updateGcmId(@Field("old") String oldId, @Field("new") String newId);
}
