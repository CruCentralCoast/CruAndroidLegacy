package org.androidcru.crucentralcoast.notifications;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import rx.Observable;
import rx.Observer;
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
        retrieveGCMId(Observers.create((token) -> {}, e -> {
            Timber.e(e, "GCM Retrieval failed");
            SharedPreferencesUtil.writeSentTokenToServer(false);
        }, () -> {
            Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }), this);
    }

    public static void retrieveGCMId(Observer<String> observer, Context context)
    {
        retrieveGCMId(context)
            .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
            .subscribe(observer);
    }

    private static Observable<String> retrieveGCMId(Context context)
    {
        return Observable.fromCallable(() -> {
            InstanceID instanceID = InstanceID.getInstance(context);
            String token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Timber.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            SharedPreferencesUtil.writeGCMID(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            SharedPreferencesUtil.writeSentTokenToServer(true);

            return token;
        })
        .flatMap(token -> {
            return setupGcmPubSub(token, context)
                    .flatMap(gcmPubSub -> Observable.just(token));
        });
    }

    private static Observable<GcmPubSub> setupGcmPubSub(String token, Context context)
    {
        return Observable.fromCallable(() -> {
            GcmPubSub gcmPubSub  = GcmPubSub.getInstance(context);
            for (String topic : TOPICS) {
                gcmPubSub.subscribe(token, "/topics/" + topic, null);
            }
            return gcmPubSub;
        })
        .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR));
    }

    public static void subscribeToMinistry(Observer<Boolean> observer, Context context, final String topic)
    {
        Observable.concat(
                (SharedPreferencesUtil.getGCMID().isEmpty()) ? Observable.empty() : Observable.just(SharedPreferencesUtil.getGCMID()),
                retrieveGCMId(context)
            )
            .take(1)
            .flatMap(token -> {
                return setupGcmPubSub(token, context)
                    .flatMap(gcmPubSub -> {
                        return Observable.fromCallable(() -> {
                            gcmPubSub.subscribe(token, "/topics/" + topic, null);
                            return true;
                        });
                    });
            })
            .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);


    }

    public static void unsubscribeToMinistry(Observer<Boolean> observer, Context context, final String topic)
    {
        Observable.concat(
                (SharedPreferencesUtil.getGCMID().isEmpty()) ? Observable.empty() : Observable.just(SharedPreferencesUtil.getGCMID()),
                retrieveGCMId(context)
            )
            .take(1)
            .flatMap(token -> {
                return setupGcmPubSub(token, context)
                        .flatMap(gcmPubSub -> {
                            return Observable.fromCallable(() -> {
                                gcmPubSub.unsubscribe(token, "/topics/" + topic);
                                return true;
                            });
                        });
            })
            .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);

    }
}
