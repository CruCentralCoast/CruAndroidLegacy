package org.androidcru.crucentralcoast.presentation.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.notifications.RegistrationIntentService;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity
{
    @Bind(R.id.logo) protected ImageView logo;
    @Bind(R.id.central_coast) protected TextView centralCoast;
    private SharedPreferences mSharedPreferences;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        this.mSharedPreferences = getSharedPreferences(CruApplication.retrievePackageName(), Context.MODE_PRIVATE);

        centralCoast.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/FreigSanProLig.otf"));
        switchToApp();
    }

    public void switchToApp()
    {
        final Intent intent = new Intent(this, MainActivity.class);

        // Determine if the app has launched before.
        if (mSharedPreferences.getBoolean(CruApplication.FIRST_LAUNCH, false))
            intent.setClass(this, MainActivity.class);
        else
            intent.setClass(this, SubscriptionStartupActivity.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final long DELAY_SECONDS = 2l;

        Observable.timer(DELAY_SECONDS, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> {
                if (checkPlayServices())
                {
                    // Start IntentService to register this application with GCM.
                    Intent service = new Intent(this, RegistrationIntentService.class);
                    startService(service);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
    }

    /**
     * Determines if the current device is enabled for google play services and if not, will prompt
     * the user to enable it.
     *
     * @return true if enabled, otherwise false
     */
    private boolean checkPlayServices()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Logger.d("This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
