package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiProvider
{
    private CruApiService service;
    private static ApiProvider instance;

    public static ApiProvider getInstance() {
        if(instance == null)
            instance = new ApiProvider();
        return instance;
    }

    private ApiProvider()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .client(CruApplication.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(CruApiService.class);
    }

    public CruApiService getService() {
        return service;
    }

}
