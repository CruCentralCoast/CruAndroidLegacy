package org.androidcru.crucentralcoast.data.providers;


import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class ResourceProvider
{
    private static CruApiService cruApiService = ApiProvider.getService();

    public static Observable<List<Resource>> getResourceByType(Resource.ResourceType type)
    {
        return cruApiService.getResources()
                .compose(RxComposeUtil.network())
                .flatMap(resources -> Observable.from(resources))
                .filter(resource -> resource.resourceType == type)
                .observeOn(Schedulers.io())
                .map(resource1 -> {
                    getResourceTagByResourceId(resource1.id)
                            .subscribeOn(Schedulers.io())
                            .toBlocking()
                            .subscribe(tag -> {
                                resource1.tags.add(tag.title);
                            });
                    return resource1;
                })
                .toList();
    }

    public static Observable<ResourceTag> getResourceTagByResourceId(String resourceId)
    {
        return cruApiService.findSingleResourceTag(resourceId)
                .compose(RxComposeUtil.network())
                .flatMap(resourceTags -> Observable.from(resourceTags));
    }
}
