package org.androidcru.crucentralcoast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.jakewharton.threetenabp.AndroidThreeTen;

import net.ypresto.timbertreeutils.CrashlyticsLogTree;

import org.androidcru.crucentralcoast.data.converters.DirectionConverter;
import org.androidcru.crucentralcoast.data.converters.GenderConverter;
import org.androidcru.crucentralcoast.data.converters.ResourceTypeConverter;
import org.androidcru.crucentralcoast.data.converters.ZonedDateTimeConverter;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;
import org.androidcru.crucentralcoast.util.PrettyDebugTree;
import org.threeten.bp.ZonedDateTime;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class CruApplication extends MultiDexApplication
{
    public static Gson gson;
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

            Stetho.initializeWithDefaults(this);
        }
        else
        {
            Fabric.with(this, new Crashlytics());

            //Send all Timber logs with a level of INFO or higher to Fabric.io
            Timber.plant(new CrashlyticsLogTree(Log.INFO));
        }
    }

    private void setupOkHttp()
    {
        if(BuildConfig.DEBUG)
        {
            okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
        }
        else
        {
            okHttpClient = new OkHttpClient();
        }
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

    private void setupGson()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeConverter());
        builder.registerTypeAdapter(Ride.Direction.class, new DirectionConverter());
        builder.registerTypeAdapter(Ride.Gender.class, new GenderConverter());
        builder.registerTypeAdapter(Resource.ResourceType.class, new ResourceTypeConverter());
        builder.addDeserializationExclusionStrategy(new SerializedNameExclusionStrategy());
        builder.addSerializationExclusionStrategy(new SerializedNameExclusionStrategy());
        builder.setPrettyPrinting();
        gson = builder.create();
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

    private class SerializedNameExclusionStrategy implements ExclusionStrategy
    {

        @Override
        public boolean shouldSkipField(FieldAttributes f)
        {
            return f.getAnnotation(SerializedName.class) == null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz)
        {
            return false;
        }
    }

}
