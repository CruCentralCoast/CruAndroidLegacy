package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import java.util.ArrayList;

import rx.Observable;

public final class MinistryTeamProvider
{
    private static CruApiService mCruService = ApiProvider.getService();

    /**
     * Gets the list of ministry teams from the sever
     * @return list of ministry teams
     */
    public static Observable<ArrayList<MinistryTeam>> requestMinistryTeams()
    {
        return mCruService.getMinistryTeams()
                .compose(RxComposeUtil.network());
    }

    /**
     * Gets the list of ministry team leaders for the provided ministry id
     * @return list of ministry team leaders for the ministry team id
     */
    public static Observable<ArrayList<CruUser>> requestMinistryTeamLeaders(String ministryTeamId)
    {
        ArrayList<String> ministryTeamIdList = new ArrayList<>();
        ministryTeamIdList.add(ministryTeamId);

        return mCruService.getMinistryTeamLeaders(ministryTeamIdList)
                .compose(RxComposeUtil.network());
    }

}
