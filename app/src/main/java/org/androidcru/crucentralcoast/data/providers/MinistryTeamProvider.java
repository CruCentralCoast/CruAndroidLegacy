package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
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
                .flatMap(ministryTeams -> Observable.from(ministryTeams))
                .flatMap(ministryTeam -> {
                    return requestMinistryTeamLeaders(ministryTeam.id)
                            //.subscribeOn(Schedulers.immediate())
                            .flatMap(users -> Observable.from(users))
                            .map(user -> {
                                if(ministryTeam.ministryTeamLeaders == null)
                                    ministryTeam.ministryTeamLeaders = new ArrayList<CruUser>();
                                ministryTeam.ministryTeamLeaders.add(user);
                                return ministryTeam;
                            })
                            .switchIfEmpty(Observable.just(ministryTeam));
                })
                .compose(RxComposeUtil.toListOrEmpty())
                .compose(RxComposeUtil.network());
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
        Query query = new Query();
        query.conditions = new ConditionsBuilder()
                .setCombineOperator(ConditionsBuilder.OPERATOR.AND)
                .addRestriction(new ConditionsBuilder()
                    .setField(CruUser.sIsMinistryTeamLeader)
                    .addRestriction(ConditionsBuilder.OPERATOR.EQUALS, Boolean.TRUE))
                .addRestriction(new ConditionsBuilder()
                    .setField(CruUser.sMinistryTeams)
                    .addRestriction(ConditionsBuilder.OPERATOR.IN, new String[]{ministryTeamId})).build();

        return cruService.getMinistryTeamLeaders(query)
                .compose(RxComposeUtil.network());
    }


    public static void joinMinistryTeam(Observer<Void> observer, String id, CruUser user)
    {
        cruService.joinMinistryTeam(id, user)
                .compose(RxComposeUtil.network())
                .subscribe(observer);
    }

}
