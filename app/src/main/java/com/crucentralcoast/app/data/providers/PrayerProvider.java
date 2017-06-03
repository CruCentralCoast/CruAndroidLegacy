package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.PrayerRequest;
import com.crucentralcoast.app.data.models.PrayerResponse;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;
import com.crucentralcoast.app.presentation.views.base.SubscriptionsHolder;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by brittanyberlanga on 5/6/17.
 */

public final class PrayerProvider {
    private static CruApiService cruApiService = CruApiProvider.getService();

    private static Observable<List<PrayerRequest>> getPrayerRequests(String leaderAPIKey,
                                                                     String leaderId) {
        return cruApiService.getPrayerRequests(leaderAPIKey, leaderId);
    }

    private static Observable<PrayerRequest> getPrayerRequestDetails(String prayerRequestId,
                                                                     String leaderAPIKey,
                                                                     String fcmId) {
        return cruApiService.getPrayerRequestDetails(prayerRequestId, leaderAPIKey, fcmId);
    }

    private static Observable<List<PrayerRequest>> getUserPrayerRequests(String fcmId) {
        return cruApiService.getUserPrayerRequests(fcmId);
    }

    private static Observable<PrayerRequest> createPrayerRequest(PrayerRequest prayerRequest) {
        return cruApiService.postPrayerRequest(prayerRequest);
    }

    private static Observable<PrayerResponse> createPrayerResponse(PrayerResponse prayerResponse,
                                                                   String leaderAPIKey) {
        return cruApiService.postPrayerResponse(prayerResponse, leaderAPIKey);
    }

    private static Observable<PrayerRequest> setPrayerRequestContactLeader(String prayerRequestId,
                                                                           String leaderAPIKey,
                                                                           String contactLeaderId) {
        return cruApiService.patchPrayerRequestContactLeader(prayerRequestId, leaderAPIKey,
                contactLeaderId);
    }

    private static Observable<PrayerRequest> setPrayerRequestContacted(String prayerRequestId,
                                                                       String leaderAPIKey,
                                                                       boolean contacted) {
        return cruApiService.patchPrayerRequestContacted(prayerRequestId, leaderAPIKey,
                contacted);
    }

    public static void setPrayerRequestContactLeader(Observer<PrayerRequest> observer,
                                                     String prayerRequestId, String leaderAPIKey,
                                                     String contactLeaderId) {
        setPrayerRequestContactLeader(prayerRequestId, leaderAPIKey, contactLeaderId)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    public static void setPrayerRequestContacted(Observer<PrayerRequest> observer,
                                                 String prayerRequestId, String leaderAPIKey,
                                                 boolean contacted) {
        setPrayerRequestContacted(prayerRequestId, leaderAPIKey, contacted)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    public static void createPrayerRequest(Observer<PrayerRequest> observer,
                                           PrayerRequest prayerRequest) {
        createPrayerRequest(prayerRequest)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    public static void createPrayerResponse(Observer<PrayerResponse> observer,
                                            PrayerResponse prayerResponse, String leaderAPIKey) {
        createPrayerResponse(prayerResponse, leaderAPIKey)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
    }

    public static void requestPrayerRequests(SubscriptionsHolder holder,
                                             Observer<List<PrayerRequest>> observer,
                                             String leaderAPIKey, String leaderId) {
        Subscription s = getPrayerRequests(leaderAPIKey, leaderId)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestPrayerRequestDetails(SubscriptionsHolder holder,
                                                   Observer<PrayerRequest> observer,
                                                   String prayerRequestId, String leaderAPIKey,
                                                   String fcmId) {
        Subscription s = getPrayerRequestDetails(prayerRequestId, leaderAPIKey, fcmId)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }

    public static void requestMyPrayerRequests(SubscriptionsHolder holder,
                                               Observer<List<PrayerRequest>> observer,
                                               String fcmId) {
        Subscription s = getUserPrayerRequests(fcmId)
                .compose(RxComposeUtil.network())
                .compose(RxComposeUtil.ui())
                .subscribe(observer);
        holder.addSubscription(s);
    }
}
