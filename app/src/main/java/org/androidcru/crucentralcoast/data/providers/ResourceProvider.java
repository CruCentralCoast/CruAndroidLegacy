package org.androidcru.crucentralcoast.data.providers;


import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.List;

import rx.Observable;

public final class ResourceProvider
{
    private static CruApiService cruApiService = ApiProvider.getService();

    public static Observable<List<Resource>> findResourceByType(Resource.ResourceType... types)
    {

        String[] stringTypes = new String[types.length];
        for(int i = 0; i < types.length; i++)
        {
            stringTypes[i] = types[i].toString();
        }

        Query query = new Query.Builder()
                .setCondition(new ConditionsBuilder()
                    .setField("type")
                    .addRestriction(ConditionsBuilder.OPERATOR.IN, stringTypes)
                    .build())
                .build();

        return cruApiService.findResources(query.conditions)
                .flatMap(resources -> Observable.from(resources))
                .map(resource -> {

                    Query tagQuery = new Query.Builder()
                            .setCondition(new ConditionsBuilder()
                                    .setField("_id")
                                    .addRestriction(ConditionsBuilder.OPERATOR.IN, resource.tagIds.toArray(new String[resource.tagIds.size()]))
                                    .build())
                            .build();

                    resource.tags = cruApiService.findResourceTag(tagQuery.conditions)
                        .compose(RxComposeUtil.network())
                        .toBlocking()
                        .first();

                    return resource;
                })
                .compose(RxLoggingUtil.log("RESOURCES"))
                .toList()
                .compose(RxComposeUtil.network());
    }

    public static Observable<List<ResourceTag>> getResourceTags()
    {
        return cruApiService.getResourceTag()
                .compose(RxComposeUtil.network());
    }
}
