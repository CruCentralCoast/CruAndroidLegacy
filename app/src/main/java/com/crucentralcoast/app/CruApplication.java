package com.crucentralcoast.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.crashlytics.android.Crashlytics;
import com.crucentralcoast.app.data.converters.DayOfWeekConverter;
import com.crucentralcoast.app.data.converters.DirectionConverter;
import com.crucentralcoast.app.data.converters.GenderConverter;
import com.crucentralcoast.app.data.converters.QuestionTypeConverter;
import com.crucentralcoast.app.data.converters.ResourceTypeConverter;
import com.crucentralcoast.app.data.converters.RideStatusConverter;
import com.crucentralcoast.app.data.converters.SnippetConverter;
import com.crucentralcoast.app.data.converters.ZonedDateTimeConverter;
import com.crucentralcoast.app.data.models.MinistryQuestion;
import com.crucentralcoast.app.data.models.Resource;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.RideCheckResponse;
import com.crucentralcoast.app.data.models.youtube.Snippet;
import com.crucentralcoast.app.notifications.RegistrationIntentService;
import com.crucentralcoast.app.util.PrettyDebugTree;
import com.crucentralcoast.app.util.SerializedNameExclusionStrategy;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.picasso.Picasso;

import net.ypresto.timbertreeutils.CrashlyticsLogExceptionTree;
import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class CruApplication extends Application {
    public static Gson gson = setupGson();

    public static OkHttpClient okHttpClient;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        AndroidThreeTen.init(this);

        setupDebugConfig();

        setupDiskCache();
        setupGson();
        setupOkHttp();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();

        Picasso.setSingletonInstance(picasso);

        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            Intent service = new Intent(this, RegistrationIntentService.class);
            startService(service);
        }
    }

    public static Context getContext() {
        return context;
    }

    /**
     * Sets up Debug tools, only if the build is for debugging.
     */
    private void setupDebugConfig() {
        //TODO turn this back to release mode
        Timber.plant(new PrettyDebugTree());
        Fabric.with(this, new Crashlytics());

        //Send all Timber logs with a level of INFO or higher to Fabric.io
        Timber.plant(new CrashlyticsLogTree(Log.DEBUG));
        //If there's a Exception sent to Timber.e(), it gets sent to Crashlytics as a non-fatal crash
        Timber.plant(new CrashlyticsLogExceptionTree());
    }

    public static OkHttpClient setupOkHttp() {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    //10MB cache
                    .cache(new Cache(new File(context.getCacheDir(), "HttpResponseCache"), 10 * 1024 * 1024))
                    .addInterceptor(interceptor)
                    .build();
        }
        else {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    /**
     * Important because it's essential that SharedPreferences doesn't get overwritten
     * @return
     */
//    public static SharedPreferences getSharedPreferences()
//    {
//        return sharedPreferences;
//    }


    /**
     * Determines if the current device is enabled for google play services and if not, will prompt
     * the user to enable it.
     *
     * @return true if enabled, otherwise false
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        SharedPreferencesUtil.writePlayServicesCode(resultCode);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void setupDiskCache() {
        try {
            Reservoir.init(this, 10240, gson); //in bytes
        }
        catch (Exception e) {
            Timber.e(e, "Not enough space for disk cache!");
        }
    }

    public static Gson setupGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeConverter());
        builder.registerTypeAdapter(Ride.Direction.class, new DirectionConverter());
        builder.registerTypeAdapter(Ride.Gender.class, new GenderConverter());
        builder.registerTypeAdapter(Resource.ResourceType.class, new ResourceTypeConverter());
        builder.registerTypeAdapter(Snippet.class, new SnippetConverter());
        builder.registerTypeAdapter(RideCheckResponse.RideStatus.class, new RideStatusConverter());
        builder.registerTypeAdapter(MinistryQuestion.Type.class, new QuestionTypeConverter());
        builder.registerTypeAdapter(RideCheckResponse.RideStatus.class, new RideStatusConverter());
        builder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekConverter());
        builder.addDeserializationExclusionStrategy(new SerializedNameExclusionStrategy());
        builder.addSerializationExclusionStrategy(new SerializedNameExclusionStrategy());
        builder.setPrettyPrinting();

        return builder.create();
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
