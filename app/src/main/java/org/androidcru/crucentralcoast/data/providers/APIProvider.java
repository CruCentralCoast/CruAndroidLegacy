package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.MainApplication;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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
                .addConverterFactory(GsonConverterFactory.create(MainApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(CruApiService.class);
    }

    public CruApiService getService() {
        return service;
    }

}
