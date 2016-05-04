package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class UserProvider
{
    private static CruApiService cruService = ApiProvider.getService();

    public static void requestCruUser(SubscriptionsHolder holder, Observer<CruUser> observer, String phoneNumber)
    {
        Subscription s = requestCruUser(phoneNumber)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    protected static Observable<CruUser> requestCruUser(String phoneNumber)
    {
        return cruService.getCruUser(phoneNumber)
                .filter(cruUser -> cruUser != null)
                .compose(RxComposeUtil.network());
    }
}
