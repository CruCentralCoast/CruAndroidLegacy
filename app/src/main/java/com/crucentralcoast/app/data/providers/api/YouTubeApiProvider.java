package com.crucentralcoast.app.data.providers.api;

import com.crucentralcoast.app.BuildConfig;
import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.data.services.YouTubeDataService;

import io.fabric.sdk.android.services.concurrency.AsyncTask;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class YouTubeApiProvider
{
    private static YouTubeDataService service;

    public static YouTubeDataService getService()
    {
        if(service == null)
        {
            setBaseUrl(BuildConfig.YOUTUBEBASEURL);
        }

        return service;
    }

    public static YouTubeDataService setBaseUrl(String baseUrl)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(CruApplication.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)))
                .build();

        service = retrofit.create(YouTubeDataService.class);

        return service;
    }
}
