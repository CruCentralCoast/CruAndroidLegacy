package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.LoginResponse;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import rx.Observable;
import rx.Observer;

public class LoginProvider
{
    private static CruApiService service = ApiProvider.getService();

    public static void login(SubscriptionsHolder holder, Observer<LoginResponse> observer,
                             String username, String password, String gcmId)
    {
        holder.addSubscription(login(username, password, gcmId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer));
    }

    static Observable<LoginResponse> login(String username, String password, String gcmId)
    {
        return service.signin(username, password, gcmId)
                .compose(RxComposeUtil.network());
    }

    public static void logout(SubscriptionsHolder holder, Observer<LoginResponse> observer)
    {
        holder.addSubscription(logout()
                .compose(RxComposeUtil.ui())
                .subscribe(observer));
    }

    static Observable<LoginResponse> logout()
    {
        return service.signout()
                .compose(RxComposeUtil.network());
    }
}
