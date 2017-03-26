package com.crucentralcoast.app.presentation.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.presentation.views.onboarding.OnboardingActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

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
            intent.setClass(this, OnboardingActivity.class);

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
