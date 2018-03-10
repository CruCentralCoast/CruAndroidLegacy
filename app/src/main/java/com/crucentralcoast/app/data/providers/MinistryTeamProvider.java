package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
                .flatMap(Observable::from)
                .compose(RxComposeUtil.toListOrEmpty());
    }


    public static void joinMinistryTeam(Observer<Void> observer, String id, CruUser user)
    {
        cruService.joinMinistryTeam(id, user)
                .compose(RxComposeUtil.network())
                .subscribe(observer);
    }

    public static Observable<MinistryTeam> getMinistryTeam(String id) {
        return cruService.getMinistryTeam(id)
                .flatMap(group -> {
                    if(group != null) {
                        return Observable.just(group);
                    }
                    else {
                        return Observable.empty();
                    }
                });

    }

    public static void getMinistryTeam(SubscriptionsHolder holder,
                                         Observer<MinistryTeam> observer,
                                         String id) {

        Subscription s = getMinistryTeam(id)
                .compose(RxComposeUtil.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        holder.addSubscription(s);
    }
}


