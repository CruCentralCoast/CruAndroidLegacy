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
            return cruApiService.getCommunityGroups(requiredMinistryQuestionAnswers.get(0).ministryQuestion.ministry, new CommunityGroupRequest(requiredMinistryQuestionAnswers))
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
}
