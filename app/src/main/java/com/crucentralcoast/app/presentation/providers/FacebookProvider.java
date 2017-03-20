package com.crucentralcoast.app.presentation.providers;

import android.content.Intent;

import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.Holder;
import com.crucentralcoast.app.data.models.facebook.FBGuestList;
import com.crucentralcoast.app.presentation.views.dialogs.RsvpDialog;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

//REVIEW singleton class that does not need to be singleton, static methods should do
public final class FacebookProvider
{
    private static CallbackManager callbackManager = com.facebook.CallbackManager.Factory.create();

    static {
        FacebookSdk.sdkInitialize(CruApplication.getContext());
    }

    public static void setupTokenCallback(Observer<LoginResult> loginResultObserver)
    {
        Observable.create(new Observable.OnSubscribe<LoginResult>()
        {
            @Override
            public void call(Subscriber<? super LoginResult> subscriber)
            {
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        SharedPreferencesUtil.writeFBAccessToken(loginResult.getAccessToken().getToken());
                        subscriber.onNext(loginResult);
                    }

                    @Override
                    public void onCancel()
                    {
                        Timber.d("User Cancelled Logging in");
                    }

                    @Override
                    public void onError(FacebookException error)
                    {
                        Timber.e(error, "Facebook SDK error");
                    }
                });
            }
        }).subscribe(loginResultObserver);
    }

    public static void tokenReceived(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static boolean isTokenValid()
    {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public static void invalidate()
    {
        SharedPreferencesUtil.removeFBTokenKey();
    }

    public static String getEventId(String eventURL)
    {
        if (eventURL.charAt(eventURL.length() - 1) == '/')
        {
            eventURL = eventURL.substring(0, eventURL.length() - 1);
        }
        return eventURL.substring(eventURL.lastIndexOf("/") + 1);
    }

    public static Observable<RsvpDialog.RSVP_STATUS> getEventStatus(String eventURL)
    {
        String eventId = getEventId(eventURL);

        Timber.d("eventId: " + eventId);

        if(FacebookProvider.isTokenValid())
        {

            return Observable.create(new Observable.OnSubscribe<RsvpDialog.RSVP_STATUS>()
            {
                @Override
                public void call(Subscriber<? super RsvpDialog.RSVP_STATUS> subscriber)
                {
                    final Holder<RsvpDialog.RSVP_STATUS> status = new Holder<>(RsvpDialog.RSVP_STATUS.NO_REPLY);

                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/" + eventId + "/attending/" + AccessToken.getCurrentAccessToken().getUserId(),
                            null,
                            HttpMethod.GET,
                            response -> {
                                FBGuestList guestList = CruApplication.gson.fromJson(response.getRawResponse(), FBGuestList.class);
                                if(!guestList.data.isEmpty())
                                {
                                    status.hold(RsvpDialog.RSVP_STATUS.ATTENDING);
                                }
                            }
                    ).executeAndWait();

                    if(status.held() == RsvpDialog.RSVP_STATUS.NO_REPLY)
                    {
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/" + eventId + "/interested/" + AccessToken.getCurrentAccessToken().getUserId(),
                                null,
                                HttpMethod.GET,
                                response -> {
                                    FBGuestList guestList = CruApplication.gson.fromJson(response.getRawResponse(), FBGuestList.class);
                                    if(!guestList.data.isEmpty())
                                    {
                                        status.hold(RsvpDialog.RSVP_STATUS.INTERESTED);
                                    }
                                }
                        ).executeAndWait();
                    }

                    subscriber.onNext(status.held());
                }
            })
            .subscribeOn(Schedulers.io());
        }
        return Observable.empty();
    }

    public static Observable<RsvpDialog.RSVP_STATUS> setRSVPStatus(String eventURL, RsvpDialog.RSVP_STATUS rsvpStatus)
    {
        String eventId = getEventId(eventURL);

        Timber.d("eventId: " + eventId);



        if(FacebookProvider.isTokenValid())
        {
            return Observable.create(new Observable.OnSubscribe<RsvpDialog.RSVP_STATUS>()
            {
                @Override
                public void call(Subscriber<? super RsvpDialog.RSVP_STATUS> subscriber)
                {
                    final Holder<String> endpoint = new Holder<>(null);

                    switch (rsvpStatus)
                    {
                        case ATTENDING:
                            endpoint.hold("attending");
                            break;
                        case INTERESTED:
                            endpoint.hold("maybe");
                            break;
                        default:
                            endpoint.hold("declined");
                            break;
                    }

                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/" + eventId + "/" + endpoint.held(),
                            null,
                            HttpMethod.POST,
                            response -> {
                                Timber.d(response.getRawResponse());
                            }
                    ).executeAndWait();

                    subscriber.onNext(rsvpStatus);
                }
            })
            .subscribeOn(Schedulers.io());
        }
        return Observable.empty();
    }

    public static Set<String> getPermissions()
    {
        return AccessToken.getCurrentAccessToken().getPermissions();
    }

}
