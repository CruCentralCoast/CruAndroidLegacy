package org.androidcru.crucentralcoast.data.providers;


import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.OptionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class ResourceProvider
{
    private static CruApiService cruApiService = CruApiProvider.getService();

    private static Observable.Transformer<Resource, Resource> tagRetriever =
        (Observable<Resource> o) -> o.map(resource -> {
                Query tagQuery = new Query.Builder()
                        .setCondition(new ConditionsBuilder()
                                .setField("_id")
                                .addRestriction(ConditionsBuilder.OPERATOR.IN, resource.tagIds.toArray(new String[resource.tagIds.size()]))
                                .build())
                        .build();

                resource.tags = cruApiService.findResourceTag(tagQuery)
                        .compose(RxComposeUtil.network())
                        .toBlocking()
                        .first();

                return resource;
            });

    public static void findResources(SubscriptionsHolder holder, Observer<List<Resource>> observer, List<Resource.ResourceType> types, List<ResourceTag> tags)
    {
        Subscription s = findResources(types, tags)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<Resource>> findResources(List<Resource.ResourceType> types, List<ResourceTag> tags)
    {
        ConditionsBuilder conditionsBuilder = new ConditionsBuilder()
                .setCombineOperator(ConditionsBuilder.OPERATOR.AND);

        if(types != null && !types.isEmpty())
        {
            String[] stringTypes = new String[types.size()];
            for (int i = 0; i < types.size(); i++)
            {
                stringTypes[i] = types.get(i).toString();
            }

            conditionsBuilder
                    .addRestriction(new ConditionsBuilder()
                            .setField("type")
                            .addRestriction(ConditionsBuilder.OPERATOR.IN, stringTypes));
        }

        if(tags != null && !tags.isEmpty())
        {
            String[] stringTags = new String[tags.size()];
            for (int i = 0; i < tags.size(); i++)
            {
                stringTags[i] = tags.get(i).id;
            }

            conditionsBuilder
                    .addRestriction(new ConditionsBuilder()
                            .setField("tags")
                            .addRestriction(ConditionsBuilder.OPERATOR.IN, stringTags));

        }

        Query query = new Query.Builder()
                .setCondition(conditionsBuilder.build())
                .build();



        return cruApiService.findResources(query)
                .flatMap(resources -> Observable.from(resources))
                .compose(tagRetriever)
                .compose(RxComposeUtil.toListOrEmpty())
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

    protected static Observable<Resource> getResources()
    {
        return cruApiService.getResources()
                .flatMap(resources -> Observable.from(resources))
                .compose(RxComposeUtil.network())
                .compose(tagRetriever);
    }

    protected static Observable<Resource> getResourcesPaginated(int page, int pageSize)
    {
        Query query = new Query.Builder()
                .setOptions(new OptionsBuilder()
                        .addOption(OptionsBuilder.OPTIONS.SKIP, page * pageSize)
                        .build())
                .build();

        return cruApiService.findResources(query)
                .flatMap(resources -> Observable.from(resources))
                .compose(tagRetriever)
                .compose(RxComposeUtil.network());
    }
}
