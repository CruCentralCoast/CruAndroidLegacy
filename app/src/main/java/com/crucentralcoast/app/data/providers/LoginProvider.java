package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.LoginResponse;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import rx.Observable;
import rx.Observer;

public class LoginProvider {
    private static CruApiService service = CruApiProvider.getService();

    public static void login(SubscriptionsHolder holder, Observer<LoginResponse> observer,
                             String username, String password, String gcmId) {
        holder.addSubscription(login(username, password, gcmId)
                .compose(RxComposeUtil.ui())
                .subscribe(observer));
    }

    static Observable<LoginResponse> login(String username, String password, String gcmId) {
        return service.signin(username, password, gcmId)
                .compose(RxComposeUtil.network());
    }

    public static void logout(SubscriptionsHolder holder, Observer<LoginResponse> observer) {
        holder.addSubscription(logout()
                .compose(RxComposeUtil.ui())
                .subscribe(observer));
    }

    static Observable<LoginResponse> logout() {
        return service.signout()
                .compose(RxComposeUtil.network());
    }

    public static Observable<Void> updateFcmId(String oldId, String newId) {
        return service.updateFcmId(oldId, newId)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui());
    }
}
