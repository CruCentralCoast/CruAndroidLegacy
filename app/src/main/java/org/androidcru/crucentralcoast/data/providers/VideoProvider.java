package org.androidcru.crucentralcoast.data.providers;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.data.models.VideoList;
import org.androidcru.crucentralcoast.data.providers.events.VideoDataEvent;
import org.androidcru.crucentralcoast.data.services.YouTubeDataService;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public final class VideoProvider {
    private static VideoProvider instance;


    private VideoProvider() {}

    public static VideoProvider getInstance() {
        if(instance == null)
            instance = new VideoProvider();
        return instance;
    }

    public void requestVideo(String videoId)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.YOUTUBEBASEURL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        YouTubeDataService youTubeDataService = retrofit.create(YouTubeDataService.class);

        Call<VideoList> videoListCall = youTubeDataService.getVideoMetadata("snippet", videoId, BuildConfig.YOUTUBEBROWSERAPIKEY);

        videoListCall.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Response<VideoList> response, Retrofit retrofit) {
                EventBus.getDefault().post(new VideoDataEvent(response.body()));
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.e(t, "Retrofit failed to retrieve video.");
            }
        });
    }
}
