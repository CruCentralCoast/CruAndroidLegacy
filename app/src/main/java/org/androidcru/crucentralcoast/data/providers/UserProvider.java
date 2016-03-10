package org.androidcru.crucentralcoast.data.providers;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.providers.util.RxComposeUtil;
import org.androidcru.crucentralcoast.data.services.CruApiService;

import rx.Observable;

public class UserProvider
{
    private static CruApiService cruService = ApiProvider.getService();

    public static Observable<CruUser> requestCruUser(String phoneNumber)
    {
        return cruService.getCruUser(phoneNumber)
                .compose(RxComposeUtil.network());
    }
}
