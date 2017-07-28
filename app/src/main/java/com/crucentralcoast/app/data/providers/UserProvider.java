package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class UserProvider
{
    private static CruApiService cruService = CruApiProvider.getService();

    public static void requestCruUser(SubscriptionsHolder holder, Observer<CruUser> observer, String phoneNumber)
    {
        Subscription s = requestCruUser(phoneNumber)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestCruUser(Observer<CruUser> observer, String phoneNumber)
    {
        requestCruUser(phoneNumber)
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }


    protected static Observable<CruUser> requestCruUser(String phoneNumber)
    {
        return cruService.getCruUser(phoneNumber)
                .filter(cruUser -> cruUser != null)
                .compose(RxComposeUtil.network());
    }
}
