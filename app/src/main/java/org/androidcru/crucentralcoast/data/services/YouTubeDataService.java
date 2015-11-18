package org.androidcru.crucentralcoast.data.services;

import org.androidcru.crucentralcoast.data.models.VideoList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface YouTubeDataService {
    @GET("youtube/v3/videos")
    Call<VideoList> getVideoMetadata(@Query("part") String type, @Query("id") String videoId, @Query("key") String developerKey);
}
