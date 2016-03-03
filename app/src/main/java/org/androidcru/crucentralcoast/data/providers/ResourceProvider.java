package org.androidcru.crucentralcoast.data.providers;


import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public final class ResourceProvider
{
    private static CruApiService cruApiService = ApiProvider.getService();

    public static Observable<List<Resource>> getResourceByType(Resource.ResourceType type)
    {
        return cruApiService.getResources()
                .subscribeOn(Schedulers.io())
                .flatMap(resources -> Observable.from(resources))
                .filter(resource -> resource.resourceType == type)
                .toList();
    }
}
