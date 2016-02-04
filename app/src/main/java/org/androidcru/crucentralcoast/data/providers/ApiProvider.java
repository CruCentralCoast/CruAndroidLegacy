package org.androidcru.crucentralcoast.data.providers;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

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
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(CruApiService.class);
    }

    public CruApiService getService() {
        return service;
    }

}
