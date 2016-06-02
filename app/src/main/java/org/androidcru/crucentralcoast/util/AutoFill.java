package org.androidcru.crucentralcoast.util;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.providers.UserProvider;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;

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
