package org.androidcru.crucentralcoast.data.providers;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.CruEventsList;
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
public final class CruEventsProvider
{
    private static CruEventsProvider mCruEventsProvider;

    private CruService mCruService;
    private CacheProvider<CruEventsList> mCacheProvider;

    private CruEventsProvider()
    {
        //Configures RetroFit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.CRU_SERVER)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(CruApplication.gson))
                .build();

        //Generates concrete implementation of CruService
        mCruService = retrofit.create(CruService.class);

        mCacheProvider = new CacheProvider<>("event_key", CruEventsList.class);
    }

    public static CruEventsProvider getInstance()
    {
        if (mCruEventsProvider == null)
        {
            mCruEventsProvider = new CruEventsProvider();
        }
        return mCruEventsProvider;
    }

    /**
     * Posts an EventListEvent onto the EventBus if EventList is cached or otherwise, a cold request
     * is issued.
     */
    public Observable<ArrayList<CruEvent>> requestEvents()
    {
        if(mCacheProvider.cacheExists())
        {
            Logger.d("Cache used");
            return mCacheProvider.getCache()
                    .flatMap(cruEventsList -> { return Observable.just(cruEventsList.mCruEvents); });
        }
        else
        {
            Logger.d("Cache empty, grabbing from network...");
            return forceUpdate();
        }
    }

    /**
     * Invalidates the cache and issues a cold request for a new EventList via the network.
     */
    public Observable<ArrayList<CruEvent>> forceUpdate()
    {
        Observable<ArrayList<CruEvent>> cruEventsStream = mCruService.getEvents()
                .subscribeOn(Schedulers.io())
                .cache();

        cruEventsStream
                .flatMap(cruEventsArrayList -> Observable.just(new CruEventsList(cruEventsArrayList)))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(mCacheProvider.mCachePutObserver);

        return cruEventsStream;
    }
}
