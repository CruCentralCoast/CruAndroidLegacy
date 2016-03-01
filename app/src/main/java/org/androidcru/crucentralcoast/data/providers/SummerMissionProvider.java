package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.SummerMission;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class SummerMissionProvider
{
    private static CruApiService mCruService = ApiProvider.getInstance().getService();
    private static SummerMissionProvider mInstance;

    public static SummerMissionProvider getInstance()
    {
        if(mInstance == null)
            mInstance = new SummerMissionProvider();
        return mInstance;
    }

    public Observable<ArrayList<SummerMission>> getSummerMissions()
    {
        return mCruService.getSummerMissions()
                .subscribeOn(Schedulers.io());
    }
}
