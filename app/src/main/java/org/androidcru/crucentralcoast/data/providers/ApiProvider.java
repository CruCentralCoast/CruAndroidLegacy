package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public final class ApiProvider
{
    private static CruApiService service;

    static
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .client(CruApplication.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        service = retrofit.create(CruApiService.class);
    }

    public static CruApiService getService()
    {
        return service;
    }
}
