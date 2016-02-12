package org.androidcru.crucentralcoast.presentation.providers;

import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.Holder;
import org.androidcru.crucentralcoast.presentation.viewmodels.facebook.FBGuestListVM;
import org.androidcru.crucentralcoast.presentation.views.dialogs.RsvpDialog;

import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public final class FacebookProvider
{
    private CallbackManager callbackManager = com.facebook.CallbackManager.Factory.create();

    private FacebookProvider()
    {
    }

    private static FacebookProvider instance = null;

    public static FacebookProvider getInstance()
    {
        if (instance == null)
            instance = new FacebookProvider();
        return instance;
    }

    public void setupTokenCallback(Observer<LoginResult> loginResultObserver)
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
                        CruApplication.getSharedPreferences().edit().putString(CruApplication.FB_TOKEN_KEY, loginResult.getAccessToken().getToken()).apply();
                        subscriber.onNext(loginResult);
                    }

                    @Override
                    public void onCancel()
                    {
                        Logger.d("User Cancelled Logging in");
                    }

                    @Override
                    public void onError(FacebookException error)
                    {
                        Logger.e(error, "Facebook SDK error");
                    }
                });
            }
        }).subscribe(loginResultObserver);
    }

    public void tokenReceived(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isTokenValid()
    {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void invalidate()
    {
        CruApplication.getSharedPreferences().edit().remove(CruApplication.FB_TOKEN_KEY).apply();
    }

    public String getEventId(String eventURL)
    {
        if (eventURL.charAt(eventURL.length() - 1) == '/')
        {
            eventURL = eventURL.substring(0, eventURL.length() - 1);
        }
        return eventURL.substring(eventURL.lastIndexOf("/") + 1);
    }

    public Observable<RsvpDialog.RSVP_STATUS> getEventStatus(String eventURL)
    {
        String eventId = getEventId(eventURL);

        Logger.d("eventId: " + eventId);

        if(FacebookProvider.getInstance().isTokenValid())
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
                                FBGuestListVM guestList = CruApplication.gson.fromJson(response.getRawResponse(), FBGuestListVM.class);
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
                                    FBGuestListVM guestList = CruApplication.gson.fromJson(response.getRawResponse(), FBGuestListVM.class);
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

    public Observable<RsvpDialog.RSVP_STATUS> setRSVPStatus(String eventURL, RsvpDialog.RSVP_STATUS rsvpStatus)
    {
        String eventId = getEventId(eventURL);

        Logger.d("eventId: " + eventId);



        if(FacebookProvider.getInstance().isTokenValid())
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
                                Logger.d(response.getRawResponse());
                            }
                    ).executeAndWait();

                    subscriber.onNext(rsvpStatus);
                }
            })
            .subscribeOn(Schedulers.io());
        }
        return Observable.empty();
    }

    public Set<String> getPermissions()
    {
        return AccessToken.getCurrentAccessToken().getPermissions();
    }

}
