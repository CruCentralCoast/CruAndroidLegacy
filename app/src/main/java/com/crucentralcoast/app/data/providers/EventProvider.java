package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.queries.OptionsBuilder;
import com.crucentralcoast.app.data.models.queries.Query;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.data.models.queries.ConditionsBuilder;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.crucentralcoast.app.data.converters.ZonedDateTimeConverter;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.notifications.RegistrationIntentService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

public class EventProvider
{
    private static CruApiService cruService = CruApiProvider.getService();

    protected static Observable.Transformer<CruEvent, CruEvent> getSubscriptionFilter()
    {
        return (Observable<CruEvent> o) -> o.filter(cruEvent -> {
            for(String parentMinistry : cruEvent.parentMinistrySubscriptions)
            {
                if(SharedPreferencesUtil.getMinistrySubscriptionIsSubscribed(parentMinistry))
                    return true;
            }
            return false;
        });
    }

    protected static Observable.Transformer<CruEvent, CruEvent> getRideCheckTransformer()
    {
        return cruEventObservable -> cruEventObservable
                .flatMap(cruEvent -> Observable.concat(
                                (SharedPreferencesUtil.getFCMID().isEmpty()) ? Observable.empty() : Observable.just(SharedPreferencesUtil.getFCMID()),
                                RegistrationIntentService.retrieveFCMId(CruApplication.getContext())
                            )
                            .take(1)
                            .flatMap(gcmId -> cruService.checkRideStatus(cruEvent.id, gcmId)
                                        .map(rideCheckResponse -> {
                                            cruEvent.rideStatus = rideCheckResponse.value;
                                            return rideCheckResponse;
                                        })
                                        .flatMap(response -> Observable.just(cruEvent)))

                );
    }

    public static void requestUsersEvents(SubscriptionsHolder holder, Observer<List<CruEvent>> observer)
    {
        Subscription s = requestUsersEvents()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static Observable<List<CruEvent>> requestUsersEvents()
    {
        return requestAllEvents()
                .flatMap(Observable::from)
                .compose(getSubscriptionFilter())
                .compose(getRideCheckTransformer())
                .compose(FeedProvider.getSortDateable())
                .compose(RxComposeUtil.toListOrEmpty())
                .compose(RxComposeUtil.network());
    }

    protected static Observable<List<CruEvent>> requestAllEvents()
    {
        return cruService.getEvents()
                .flatMap(Observable::from)
                .compose(FeedProvider.getSortDateable())
                .compose(RxComposeUtil.toListOrEmpty())
                .compose(RxComposeUtil.network());
    }

    protected static Observable<CruEvent> requestCruEventByID(String id)
    {
        return cruService.findSingleCruEvent(id)
                .compose(RxComposeUtil.network())
                .flatMap(Observable::from);
    }

    protected static Observable<List<CruEvent>> getEventsPaginated(ZonedDateTime fromDate, int page, int pageSize)
    {
        if(fromDate == null || page < 0 || pageSize <= 0)
        {
            Timber.e("fromDate: %s, page: %i, pageSize: %i", fromDate, page, pageSize);
            return Observable.error(new IllegalArgumentException("Invalid arguments"));
        }

        Query query = new Query.Builder()
                .setCondition(new ConditionsBuilder()
                        .setCombineOperator(ConditionsBuilder.OPERATOR.AND)
                        .addRestriction(new ConditionsBuilder()
                                .setField(CruEvent.sStartDate)
                                .addRestriction(ConditionsBuilder.OPERATOR.GTE, ZonedDateTimeConverter.format(fromDate)))
                        .addRestriction(new ConditionsBuilder()
                                .setField(CruEvent.sStartDate)
                                .addRestriction(ConditionsBuilder.OPERATOR.LT, ZonedDateTimeConverter.format(fromDate.plusWeeks(1L))))
                        .build())
                .setOptions(new OptionsBuilder()
                        .addOption(OptionsBuilder.OPTIONS.SKIP, page * pageSize)
                        .build())
                .build();

        Timber.i(query.conditions.toString());
        Timber.i(query.options.toString());

        return cruService.searchEvents(query)
                .compose(RxComposeUtil.network());
    }
}
