package org.androidcru.crucentralcoast.data.providers;

import android.content.SharedPreferences;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.OptionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class EventProvider
{
    private static CruApiService cruService = CruApiProvider.getService();

    protected static Observable.Transformer<CruEvent, CruEvent> getSubscriptionFilter(SharedPreferences sharedPreferences)
    {
        return (Observable<CruEvent> o) -> o.filter(cruEvent -> {
            for(String parentMinistry : cruEvent.parentMinistrySubscriptions)
            {
                if(sharedPreferences.getBoolean(parentMinistry, false))
                    return true;
            }
            return false;
        });
    }

    public static void requestEvents(SubscriptionsHolder holder, Observer<List<CruEvent>> observer, SharedPreferences sharedPreferences)
    {
        Subscription s = requestEvents()
                .flatMap(cruEvents -> Observable.from(cruEvents))
                .compose(getSubscriptionFilter(sharedPreferences))
                .toList()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<ArrayList<CruEvent>> requestEvents()
    {
        return cruService.getEvents()
                .compose(RxComposeUtil.network());
    }

    public static void requestCruEventByID(SubscriptionsHolder holder, Observer<CruEvent> observer, String id)
    {
        Subscription s = requestCruEventByID(id)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<CruEvent> requestCruEventByID(String id)
    {
        return cruService.findSingleCruEvent(id)
                .compose(RxComposeUtil.network())
                .flatMap(cruevents -> {
                    return Observable.from(cruevents);
                });
    }

    protected static Observable<List<CruEvent>> getEventsPaginated(ZonedDateTime fromDate, int page, int pageSize)
    {
        Query query = new Query.Builder()
                .setCondition(new ConditionsBuilder()
                        .setCombineOperator(ConditionsBuilder.OPERATOR.AND)
                        .addRestriction(new ConditionsBuilder()
                                .setField(CruEvent.sStartDate)
                                .addRestriction(ConditionsBuilder.OPERATOR.GTE, fromDate.toString()))
                        .addRestriction(new ConditionsBuilder()
                                .setField(CruEvent.sStartDate)
                                .addRestriction(ConditionsBuilder.OPERATOR.LT, fromDate.plusWeeks(1L).toString()))
                        .build())
                .setOptions(new OptionsBuilder()
                        .addOption(OptionsBuilder.OPTIONS.SKIP, page * pageSize)
                        .build())
                .build();

        return cruService.searchEvents(query)
                .compose(RxComposeUtil.network());
    }
}
