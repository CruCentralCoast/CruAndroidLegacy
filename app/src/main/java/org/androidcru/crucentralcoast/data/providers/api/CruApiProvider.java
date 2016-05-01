package org.androidcru.crucentralcoast.data.providers.api;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class CruApiProvider
{
    private static CruApiService service;

    public static CruApiService getService()
    {
        if(service == null)
        {
            setBaseUrl(BuildConfig.CRU_SERVER);
        }

        return service;
    }

    public static CruApiService setBaseUrl(String baseUrl)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(CruApplication.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        service = retrofit.create(CruApiService.class);

        return service;
    }
}
