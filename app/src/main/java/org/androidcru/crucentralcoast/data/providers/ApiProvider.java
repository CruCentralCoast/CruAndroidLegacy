package org.androidcru.crucentralcoast.data.providers;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
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
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(CruApiService.class);
    }

    public CruApiService getService() {
        return service;
    }

}
