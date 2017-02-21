package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
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

    protected static Observable<ArrayList<MinistryQuestion>> getAllQuestions() {
        return cruApiService.getMinistryQuestions()
                .compose(RxComposeUtil.network())
                .compose(RxLoggingUtil.log("MINISTRY_QUESTIONS"));
    }
}
