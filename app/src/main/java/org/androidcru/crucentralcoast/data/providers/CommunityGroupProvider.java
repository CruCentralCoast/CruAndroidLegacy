package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.data.models.CommunityGroupRequest;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.data.models.MinistryQuestionAnswer;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.providers.util.RxLoggingUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

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
}
