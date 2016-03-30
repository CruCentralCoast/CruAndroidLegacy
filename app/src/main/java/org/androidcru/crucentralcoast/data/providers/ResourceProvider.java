package org.androidcru.crucentralcoast.data.providers;


import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class ResourceProvider
{
    private static CruApiService cruApiService = ApiProvider.getService();

    public static void findResources(SubscriptionsHolder holder, Observer<List<Resource>> observer, Resource.ResourceType[] types, ResourceTag[] tags)
    {
        Subscription s = findResources(types, tags)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Resource>> findResources(Resource.ResourceType[] types, ResourceTag[] tags)
    {
        ConditionsBuilder conditionsBuilder = new ConditionsBuilder()
                .setCombineOperator(ConditionsBuilder.OPERATOR.AND);

        if(types != null && types.length > 0)
        {
            String[] stringTypes = new String[types.length];
            for (int i = 0; i < types.length; i++)
            {
                stringTypes[i] = types[i].toString();
            }

            conditionsBuilder
                    .addRestriction(new ConditionsBuilder()
                            .setField("type")
                            .addRestriction(ConditionsBuilder.OPERATOR.IN, stringTypes));
        }

        if(tags != null && tags.length > 0)
        {
            String[] stringTags = new String[tags.length];
            for (int i = 0; i < tags.length; i++)
            {
                stringTags[i] = tags[i].id;
            }

            conditionsBuilder
                    .addRestriction(new ConditionsBuilder()
                            .setField("tags")
                            .addRestriction(ConditionsBuilder.OPERATOR.IN, stringTags));

        }

        Query query = new Query.Builder()
                .setCondition(conditionsBuilder.build())
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
                .toList()
                .compose(RxComposeUtil.network());
    }

    public static void getResourceTags(SubscriptionsHolder holder, Observer<List<ResourceTag>> observer)
    {
        Subscription s = getResourceTags()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<ResourceTag>> getResourceTags()
    {
        return cruApiService.getResourceTag()
                .compose(RxComposeUtil.network());
    }
}
