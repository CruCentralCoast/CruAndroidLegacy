package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

public class MinistryTeamProvider
{
    private static CruApiService mCruService = ApiProvider.getInstance().getService();
    private static MinistryTeamProvider mInstance;

    private MinistryTeamProvider() {}

    public static MinistryTeamProvider getInstance()
    {
        if (mInstance == null)
            mInstance = new MinistryTeamProvider();
        return mInstance;
    }

    /**
     * Gets the list of ministry teams from the sever
     * @return list of ministry teams
     */
    public Observable<ArrayList<MinistryTeam>> requestMinistryTeams()
    {
        return mCruService.getMinistryTeams().subscribeOn(Schedulers.io());
    }

    /**
     * Gets the list of ministry team leaders for the provided ministry id
     * @return list of ministry team leaders for the ministry team id
     */
    public Observable<ArrayList<CruUser>> requestMinistryTeamLeaders(String ministryTeamId)
    {
        ArrayList<String> ministryTeamIdList = new ArrayList<>();
        ministryTeamIdList.add(ministryTeamId);

        return mCruService.getMinistryTeamLeaders(ministryTeamIdList);
    }

}
