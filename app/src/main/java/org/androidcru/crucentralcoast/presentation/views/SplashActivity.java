package org.androidcru.crucentralcoast.presentation.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.UserProvider;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.providers.UserProvider;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseAppCompatActivity
{
    private SharedPreferences sharedPreferences;
    @Bind(R.id.central_coast) TextView centralCoast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //REVIEW magic string, AppConstants
        ViewUtil.setFont(centralCoast, "FreigSanProLig.otf");

        this.sharedPreferences = CruApplication.getSharedPreferences();
        switchToApp();
    }

    public void switchToApp()
    {
        final Intent intent = new Intent(this, MainActivity.class);

        // Determine if the app has launched before.
        if (sharedPreferences.getBoolean(AppConstants.FIRST_LAUNCH, false))
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
