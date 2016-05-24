package org.androidcru.crucentralcoast.presentation.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseAppCompatActivity
{
    @BindView(R.id.central_coast) TextView centralCoast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        unbinder = ButterKnife.bind(this);
        //REVIEW magic string, AppConstants
        ViewUtil.setFont(centralCoast, "FreigSanProLig.otf");

        switchToApp();
    }

    public void switchToApp()
    {
        final Intent intent = new Intent(this, MainActivity.class);

        // Determine if the app has launched before.
        if (SharedPreferencesUtil.isFirstLaunch())
            intent.setClass(this, MainActivity.class);
        else
            intent.setClass(this, SubscriptionActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //REVIEW should this be moved to AppConstants?
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
