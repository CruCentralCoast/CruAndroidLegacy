package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.MinistryQuestion;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.providers.util.RxLoggingUtil;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public final class MinistryQuestionsProvider {
    private static CruApiService cruApiService = CruApiProvider.getService();

    public static void getMinistryQuestions(SubscriptionsHolder holder, Observer<List<MinistryQuestion>> observer, String id) {
        Subscription s = getMinistryQuestions(id)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<MinistryQuestion>> getMinistryQuestions(String id)
    {
        return getAllQuestions()
                .flatMap(Observable::from)
                .filter(ministryQuestion -> ministryQuestion.ministry.equals(id))
                .compose(RxComposeUtil.toListOrEmpty());
    }

    protected static Observable<List<MinistryQuestion>> getAllQuestions() {
        return cruApiService.getMinistryQuestions()
                .compose(RxComposeUtil.network())
                .compose(RxLoggingUtil.log("MINISTRY_QUESTIONS"));
    }
}
