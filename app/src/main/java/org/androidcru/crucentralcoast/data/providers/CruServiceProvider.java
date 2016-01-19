package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.MinistrySubscription;
import org.androidcru.crucentralcoast.data.services.CruService;

import java.util.ArrayList;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * EventProvider is a Singleton class designed to provide Presenters with a static reference to
 * the EventList object.
 */
public final class CruServiceProvider
{
    private static CruServiceProvider mCruServiceProvider;

    private CruService mCruService;

    private CruServiceProvider()
    {
        //Configures RetroFit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .build();

        //Generates concrete implementation of CruService
        mCruService = retrofit.create(CruService.class);
    }

    public static CruServiceProvider getInstance()
    {
        if (mCruServiceProvider == null)
        {
            mCruServiceProvider = new CruServiceProvider();
        }
        return mCruServiceProvider;
    }


    public Observable<ArrayList<CruEvent>> requestEvents()
    {
        return forceUpdateEvents();
    }

    /**
     * Invalidates the cache and issues a cold request for a new EventList via the network.
     */
    public Observable<ArrayList<CruEvent>> forceUpdateEvents()
    {
        Observable<ArrayList<CruEvent>> cruEventsStream = mCruService.getEvents()
                .subscribeOn(Schedulers.io());

        return cruEventsStream;
    }


    /**
     * Gets the list of available ministries from the server.
     * @return list of ministries
     */
    public Observable<ArrayList<MinistrySubscription>> requestMinistries()
    {
        return mCruService.getMinistries().subscribeOn(Schedulers.io());
    }

}
