package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.SummerMission;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

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
