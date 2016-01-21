package org.androidcru.crucentralcoast.presentation.providers;

import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;

import rx.Observable;
import rx.Subscriber;

public final class FacebookProvider
{
    private CallbackManager callbackManager = com.facebook.CallbackManager.Factory.create();

    private FacebookProvider() {}
    private static FacebookProvider instance = null;

    public static FacebookProvider getInstance()
    {
        if(instance == null)
            instance = new FacebookProvider();
        return instance;
    }

    public Observable<LoginResult> setupTokenCallback()
    {
        Logger.d("Attempting to decrypt accessToken from SharedPreferences");
        if(hasToken())
        {
            String accessToken= Hawk.get(CruApplication.FB_TOKEN_KEY);
            Logger.d("Decryption successful\ntoken: " + accessToken);
        }
        else
            Logger.d("accessToken not found in SharedPreferences");

        return Observable.create(new Observable.OnSubscribe<LoginResult>()
        {
            @Override
            public void call(Subscriber<? super LoginResult> subscriber)
            {
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        Logger.d(loginResult.getAccessToken().getToken());
                        Hawk.put(CruApplication.FB_TOKEN_KEY, loginResult.getAccessToken().getToken());
                        Logger.d("accessToken stored and encrypted");
                        subscriber.onNext(loginResult);
                    }

                    @Override
                    public void onCancel()
                    {

                    }

                    @Override
                    public void onError(FacebookException error)
                    {
                        Logger.e(error, "Facebook SDK error");
                    }
                });
            }
        });
    }

    public void tokenReceived(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public Boolean hasToken()
    {
        return Hawk.contains(CruApplication.FB_TOKEN_KEY);
    }
}
