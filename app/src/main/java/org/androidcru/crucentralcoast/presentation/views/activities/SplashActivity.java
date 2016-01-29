package org.androidcru.crucentralcoast.presentation.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidcru.crucentralcoast.MainApplication;
import org.androidcru.crucentralcoast.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity
{
    @Bind(R.id.logo) protected ImageView logo;
    @Bind(R.id.central_coast) protected TextView centralCoast;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        this.mSharedPreferences = MainApplication.getSharedPreferences();

        centralCoast.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/FreigSanProLig.otf"));
        switchToApp();
    }

    public void switchToApp()
    {
        final Intent intent = new Intent(this, MainActivity.class);

        // Determine if the app has launched before.
        if (mSharedPreferences.getBoolean(MainApplication.FIRST_LAUNCH, false))
            intent.setClass(this, MainActivity.class);
        else
            intent.setClass(this, SubscriptionStartupActivity.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final long DELAY_SECONDS = 2l;

        Observable.timer(DELAY_SECONDS, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> {
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
    }
}
