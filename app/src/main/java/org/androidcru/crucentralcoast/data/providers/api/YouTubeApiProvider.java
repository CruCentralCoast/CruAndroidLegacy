package org.androidcru.crucentralcoast.data.providers.api;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.services.YouTubeDataService;

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
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        service = retrofit.create(YouTubeDataService.class);

        return service;
    }
}
