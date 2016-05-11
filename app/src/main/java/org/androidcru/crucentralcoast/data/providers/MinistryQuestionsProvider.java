package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.data.providers.api.CruApiProvider;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by mitch on 5/7/16.
 */
public final class MinistryQuestionsProvider {
    private static CruApiService cruApiService = CruApiProvider.getService();

    public static void getMinistryQuestions(SubscriptionsHolder holder, Observer<List<MinistryQuestion>> observer, String ministryId) {
        Subscription s = getMinistryQuestions(ministryId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<List<MinistryQuestion>> getMinistryQuestions(String ministryId) {
        return cruApiService.getMinistryQuestions("")
                .compose(RxComposeUtil.network());
    }
}
