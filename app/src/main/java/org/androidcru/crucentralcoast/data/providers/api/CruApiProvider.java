package org.androidcru.crucentralcoast.data.providers.api;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class CruApiProvider
{
    private static CruApiService service;
    private static Retrofit.Builder builder;
    private static Scheduler networkScheduler;

    static {
        builder = new Retrofit.Builder()
                .client(CruApplication.setupOkHttp())
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson));
    }

    public static CruApiService getService()
    {
        if(service == null)
            setBaseUrl(BuildConfig.CRU_SERVER);

        if(networkScheduler == null)
            //setNetworkScheduler(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR));
            setNetworkScheduler(Schedulers.io());

        return service;
    }

    public static Retrofit.Builder setBaseUrl(String baseUrl)
    {
        builder = builder.baseUrl(baseUrl);
        service = builder.build().create(CruApiService.class);

        return builder;
    }

    public static Retrofit.Builder setNetworkScheduler(Scheduler scheduler)
    {
        networkScheduler = scheduler;
        builder = builder.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(scheduler));
        service = builder.build().create(CruApiService.class);

        return builder;
    }

    public static Scheduler getNetworkScheduler()
    {
        return networkScheduler;
    }
}
