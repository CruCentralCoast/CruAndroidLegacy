package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class MinistryTeamProvider
{
    private static CruApiService cruService = CruApiProvider.getService();

    public static void requestMinistryTeams(SubscriptionsHolder holder, Observer<List<MinistryTeam>> observer)
    {
        Subscription s = requestMinistryTeams()
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    /**
     * Gets the list of ministry teams from the server
     * @return list of ministry teams
     */
    protected static Observable<List<MinistryTeam>> requestMinistryTeams()
    {
        return cruService.getMinistryTeams()
                .compose(RxComposeUtil.network())
                .flatMap(ministryTeams -> Observable.from(ministryTeams))
                .map(ministryTeam -> {
                    ministryTeam.ministryTeamLeaders = requestMinistryTeamLeaders(ministryTeam.id)
                            .compose(RxComposeUtil.network())
                            .toBlocking()
                            .first();
                    return ministryTeam;
                })
                .toList();
    }

    public static void requestMinistryTeamLeaders(SubscriptionsHolder holder, Observer<ArrayList<CruUser>> observer, String ministryTeamId)
    {
        Subscription s = requestMinistryTeamLeaders(ministryTeamId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    /**
     * Gets the list of ministry team leaders for the provided ministry id
     * @return list of ministry team leaders for the ministry team id
     */
    protected static Observable<ArrayList<CruUser>> requestMinistryTeamLeaders(String ministryTeamId)
    {
        ArrayList<String> ministryTeamIdList = new ArrayList<>();
        ministryTeamIdList.add(ministryTeamId);

        return cruService.getMinistryTeamLeaders(ministryTeamIdList)
                .compose(RxComposeUtil.network());
    }


    public static void joinMinistryTeam(Observer<Void> observer, String id, CruUser user)
    {
        cruService.joinMinistryTeam(id, user)
                .compose(RxComposeUtil.network())
                .subscribe(observer);
    }

}
