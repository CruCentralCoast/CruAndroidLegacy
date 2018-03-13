package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CommunityGroupRequest;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;
import com.crucentralcoast.app.data.models.MinistryQuestionAnswer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class CommunityGroupProvider
{
    private static CruApiService cruApiService = CruApiProvider.getService();

    public static void getCommunityGroups(SubscriptionsHolder holder, Observer<List<CommunityGroup>> observer, List<MinistryQuestionAnswer> questionAnswers) {
        Subscription s = getCommunityGroups(questionAnswers)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<CommunityGroup>> getCommunityGroups(List<MinistryQuestionAnswer> questionAnswers)
    {
        ArrayList<MinistryQuestionAnswer> requiredMinistryQuestionAnswers = new ArrayList<>();
        for (MinistryQuestionAnswer m : questionAnswers)
        {
            if (m.ministryQuestion.required)
                requiredMinistryQuestionAnswers.add(m);
        }
        if (requiredMinistryQuestionAnswers.isEmpty())
            return Observable.empty();
        else
            return cruApiService.getCommunityGroups(requiredMinistryQuestionAnswers.get(0).ministryQuestion.ministry.get(0), new CommunityGroupRequest(requiredMinistryQuestionAnswers))
                    .flatMap(groups -> {
                        if(groups.isEmpty())
                            return Observable.empty();
                        else
                            return Observable.just(groups);
                    })
                    .compose(RxComposeUtil.network());
    }

    public static void joinCommunityGroup(Observer<Void> observer, String id, CruUser user)
    {
        cruApiService.joinCommunityGroup(id, user)
                .compose(RxComposeUtil.network())
                .subscribe(observer);
    }

    public static Observable<CommunityGroup> getCommunityGroup(String id) {
        return cruApiService.getCommunityGroup(id)
                .flatMap(group -> {
                   if(group != null) {
                       return Observable.just(group);
                   }
                   else {
                       return Observable.empty();
                   }
                });

    }

    public static void getCommunityGroup(SubscriptionsHolder holder,
                                         Observer<CommunityGroup> observer,
                                         String id) {
        Subscription s = getCommunityGroup(id)
                            .compose(RxComposeUtil.network())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
        holder.addSubscription(s);
    }
}
