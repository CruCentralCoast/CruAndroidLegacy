package com.crucentralcoast.app.data.services;

import com.crucentralcoast.app.data.models.youtube.YouTubeResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface YouTubeDataService
{
    @GET("/youtube/v3/playlistItems")
    Observable<YouTubeResponse> getPlaylistVideos(@Query("key") String developerKey, @Query("part") String part,
                                                  @Query("playlistId") String playlistId, @Query("maxResults") long maxResults,
                                                  @Query("pageToken") String nextPageToken);
    @GET("/youtube/v3/search")
    Observable<YouTubeResponse> getSearchResults(@Query("key") String developerKey, @Query("part") String part,
                                                  @Query("q") String q, @Query("channelId") String channelId, @Query("maxResults") long maxResults,
                                                  @Query("pageToken") String nextPageToken);
}
