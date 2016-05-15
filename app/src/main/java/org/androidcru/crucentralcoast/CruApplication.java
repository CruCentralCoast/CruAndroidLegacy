package org.androidcru.crucentralcoast;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.picasso.Picasso;

import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import org.androidcru.crucentralcoast.data.converters.DirectionConverter;
import org.androidcru.crucentralcoast.data.converters.GenderConverter;
import org.androidcru.crucentralcoast.data.converters.ResourceTypeConverter;
import org.androidcru.crucentralcoast.data.converters.SnippetConverter;
import org.androidcru.crucentralcoast.data.converters.ZonedDateTimeConverter;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;
import org.androidcru.crucentralcoast.util.PrettyDebugTree;
import org.androidcru.crucentralcoast.util.SerializedNameExclusionStrategy;
import org.threeten.bp.ZonedDateTime;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class CruApplication extends Application
{
    public static Gson gson = setupGson();

    public static OkHttpClient okHttpClient;
    private static Context context;
    private static SharedPreferences sharedPreferences;

    public static Context getContext()
    {
        return context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        AndroidThreeTen.init(this);

        setupDebugConfig();

        setupDiskCache();
        setupGson();
        setupOkHttp();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();

        Picasso.setSingletonInstance(picasso);


        if (checkPlayServices())
        {
            // Start IntentService to register this application with GCM.
            Intent service = new Intent(this, RegistrationIntentService.class);
            startService(service);
        }
        Timber.d(getGCMID());
    }

    /**
     * Sets up Debug tools, only if the build is for debugging.
     */
    private void setupDebugConfig()
    {
        if(BuildConfig.DEBUG)
        {
            Timber.plant(new PrettyDebugTree());
        }
        else
        {
            Fabric.with(this, new Crashlytics());

            //Send all Timber logs with a level of INFO or higher to Fabric.io
            Timber.plant(new CrashlyticsLogTree(Log.INFO));
        }
    }

    public static OkHttpClient setupOkHttp()
    {
        if(BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    //10MB cache
                    .cache(new Cache(new File(context.getCacheDir(), "HttpResponseCache"), 10 * 1024 * 1024))
                    .addInterceptor(interceptor)
                    .build();
        }
        else
        {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    /**
     * Important because it's essential that SharedPreferences doesn't get overwritten
     * @return
     */
    public static SharedPreferences getSharedPreferences()
    {
        return sharedPreferences;
    }


    /**
     * Determines if the current device is enabled for google play services and if not, will prompt
     * the user to enable it.
     *
     * @return true if enabled, otherwise false
     */
    private boolean checkPlayServices()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        sharedPreferences.edit().putInt(AppConstants.PLAY_SERVICES, resultCode).apply();
        return resultCode == ConnectionResult.SUCCESS;
    }

    private void setupDiskCache()
    {
        try
        {
            Reservoir.init(this, 10240, gson); //in bytes
        } catch (Exception e)
        {
            Timber.e(e, "Not enough space for disk cache!");
        }
    }

    public static Gson setupGson()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeConverter());
        builder.registerTypeAdapter(Ride.Direction.class, new DirectionConverter());
        builder.registerTypeAdapter(Ride.Gender.class, new GenderConverter());
        builder.registerTypeAdapter(Resource.ResourceType.class, new ResourceTypeConverter());
        builder.registerTypeAdapter(Snippet.class, new SnippetConverter());
        builder.addDeserializationExclusionStrategy(new SerializedNameExclusionStrategy());
        builder.addSerializationExclusionStrategy(new SerializedNameExclusionStrategy());
        builder.setPrettyPrinting();

        return builder.create();
    }

    public static void saveGCMID(String key)
    {
        CruApplication.getSharedPreferences().edit().putString(context.getString(R.string.gcm_registration_id), key).apply();
    }

    //TODO apparently these can change.
    public static String getGCMID()
    {
        return CruApplication.getSharedPreferences().getString(context.getString(R.string.gcm_registration_id), "");
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
