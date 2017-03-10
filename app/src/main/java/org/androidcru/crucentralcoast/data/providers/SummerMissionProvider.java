package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class SummerMissionProvider
{
    private static CruApiService mCruService = CruApiProvider.getService();

    public static void requestSummerMissions(SubscriptionsHolder holder, Observer<List<SummerMission>> observer)
    {
        Subscription s = requestSummerMissions()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<SummerMission>> requestSummerMissions()
    {
        return mCruService.getSummerMissions()
                .compose(RxComposeUtil.network());
    }
}
