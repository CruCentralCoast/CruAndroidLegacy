package com.crucentralcoast.app.notifications;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.data.providers.LoginProvider;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Observers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        retrieveFCMId(Observers.create((token) -> {
        }, e -> {
            Timber.e(e, "FCM Retrieval failed");
            SharedPreferencesUtil.writeSentTokenToServer(false);
        }, () -> {
            Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }), this);
    }

    public static void retrieveFCMId(Observer<String> observer, Context context) {
        retrieveFCMId(context)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    public static Observable<String> retrieveFCMId(Context context) {
        return Observable.fromCallable(() -> {
            String newToken = FirebaseInstanceId.getInstance().getToken();
            String oldToken = SharedPreferencesUtil.getFCMID();

            LoginProvider.updateFcmId(oldToken, newToken)
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            Timber.i("Successfully updated FCM ID!");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "Failed to update FCM ID");
                        }

                        @Override
                        public void onNext(Void aVoid) {

                        }
                    });

            SharedPreferencesUtil.writeFCMID(newToken);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            SharedPreferencesUtil.writeSentTokenToServer(true);

            return newToken;
        })
                .flatMap(token -> setupFcmPubSub(token, context)
                        .flatMap(gcmPubSub -> Observable.just(token)));
    }

    private static Observable<String> setupFcmPubSub(String token, Context context) {
        return Observable.fromCallable(() -> {
            for (String topic : TOPICS) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
            }
            return FirebaseInstanceId.getInstance().getToken();
        }).subscribeOn(Schedulers.io());
    }

    public static void subscribeToMinistry(Observer<Boolean> observer, Context context, final String topic) {
        Observable.concat(
                (SharedPreferencesUtil.getFCMID().isEmpty()) ? Observable.empty() : Observable.just(SharedPreferencesUtil.getFCMID()),
                retrieveFCMId(context)
        )
                .take(1)
                .flatMap(token ->
                        setupFcmPubSub(token, context)
                                .flatMap(gcmPubSub ->
                                        Observable.fromCallable(() -> {
                                            FirebaseMessaging.getInstance().subscribeToTopic(topic);
                                            return true;
                                        })
                                )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void unsubscribeToMinistry(Observer<Boolean> observer, Context context, final String topic) {
        Observable.concat(
                (SharedPreferencesUtil.getFCMID().isEmpty()) ? Observable.empty() : Observable.just(SharedPreferencesUtil.getFCMID()),
                retrieveFCMId(context)
        )
                .take(1)
                .flatMap(token ->
                        setupFcmPubSub(token, context)
                                .flatMap(gcmPubSub ->
                                        Observable.fromCallable(() -> {
                                            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                                            return true;
                                        })
                                )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
