package org.androidcru.crucentralcoast.data.providers;

import com.anupcowkur.reservoir.Reservoir;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * CruEventCacheProvider handles caching the EventList to disk
 */
public class CacheProvider<T>
{
    //key used to retrieve the EventList from disk
    private String mKey;
    private Class<T> mType;

    public final Observer<T> mCachePutObserver = new Observer<T>()
    {
        @Override
        public void onCompleted()
        {
            Logger.d("Cache completed.");
        }

        @Override
        public void onError(Throwable e)
        {
            Logger.e(e, "Cache RxJava onError");
        }

        @Override
        public void onNext(T t)
        {
            try
            {
                Reservoir.put(mKey, t);
                if(!Reservoir.contains(mKey))
                {
                    Logger.e("Cache write failed.");
                }
            }
            catch(Exception e)
            {
                Logger.e(e, "Reservoir error!");
            }
        }
    };

    public CacheProvider(String key, Class<T> type)
    {
        this.mKey = key;
        this.mType = type;
    }

    /**
     * Asks the disk cache if it has a copy of EventList
     * @return EventList if if the cache has an entry, null otherwise
     */
    public Observable<T> getCache()
    {
        return Observable.defer(() -> {
            try
            {
                if(cacheExists())
                {
                    T t = Reservoir.get(mKey, mType);
                    return Observable.just(t);
                }
            }
            catch(Exception e)
            {
                Logger.e(e, "Reservoir error!");
            }
            return Observable.empty();
        }).subscribeOn(Schedulers.io());
    }

    public boolean cacheExists()
    {
        try
        {
            return Reservoir.contains(mKey);
        }
        catch(Exception e)
        {
            Logger.e(e, "Reservoir error!");
        }
        return false;
    }
}
