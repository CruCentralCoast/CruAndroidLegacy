package com.crucentralcoast.app.util;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.data.providers.UserProvider;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observer;
import rx.functions.Action0;
import rx.observers.Observers;
import timber.log.Timber;

public class AutoFill
{
    public static void setupAutoFillData(BaseAppCompatActivity activity, Action0 onSuccess)
    {

        RxPermissions.getInstance(activity)
            .request(Manifest.permission.READ_PHONE_STATE)
            .subscribe(granted -> {
                if (granted)
                {
                    if (!SharedPreferencesUtil.isFirstLaunch())
                    {
                        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                        String userPhoneNumber = telephonyManager.getLine1Number();
                        if (userPhoneNumber != null && userPhoneNumber.length() >= 10) {
                            userPhoneNumber = userPhoneNumber.substring(userPhoneNumber.length() - 10, userPhoneNumber.length());

                            SharedPreferencesUtil.writeBasicInfo(null, null, userPhoneNumber);

                            Observer<CruUser> observer = Observers.create(cruUser -> {
                                SharedPreferencesUtil.writeBasicInfo(cruUser.name.firstName + " " + cruUser.name.lastName, cruUser.email, null);
                            }, e -> {
                                Timber.e(e, "Error validating phone number");
                                activity.hideAutoFillDialog();
                            }, () -> {
                                activity.hideAutoFillDialog();
                                onSuccess.call();
                            });

                            activity.displayAutoFillDialog();
                            UserProvider.requestCruUser(observer, userPhoneNumber);
                            SharedPreferencesUtil.writeFirstLaunch(true);
                        }
                    }
                    else
                    {
                        onSuccess.call();
                    }
                }
                else {
                    onSuccess.call();
                }
            });

    }
}
