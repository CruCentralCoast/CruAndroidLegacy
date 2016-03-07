package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

import rx.Observable;

public final class SummerMissionProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    public static Observable<ArrayList<SummerMission>> getSummerMissions()
    {
        return mCruService.getSummerMissions()
                .compose(RxComposeUtil.network());
    }
}
