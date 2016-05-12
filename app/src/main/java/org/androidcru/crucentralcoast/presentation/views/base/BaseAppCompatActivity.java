package org.androidcru.crucentralcoast.presentation.views.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;

import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseAppCompatActivity extends AppCompatActivity implements SubscriptionsHolder
{
    private CompositeSubscription subscriptions = new CompositeSubscription();
    public Unbinder unbinder;

    //temporary
    protected void onCreate(Bundle savedInstance) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("CruGoldTheme", "YES");

        PackageInfo packageInfo;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            String resName = this.getResources().getResourceEntryName(themeResId);

            System.out.println("resname is " + resName);

            if (!(this instanceof MainActivity) && (resName.equals("CruGoldIsBest") || resName.equals("AppTheme"))) {
                if (themeName != null && themeName.equals("YES")) {
                    setTheme(R.style.CruGoldIsBest);
                } else {
                    System.out.println("THE THEME WAS " + themeName);
                    setTheme(R.style.AppTheme);
                }
            }
//            else {
//                if (themeName != null && themeName.equals("YES")) {
//                    setTheme(R.style.CruGoldIsBestNoAction);
//                } else {
//                    System.out.println("THE THEME WAS " + themeName);
//                    setTheme(R.style.AppTheme_NoActionBar);
//                }
//            }
        }
        catch (PackageManager.NameNotFoundException e)
        {

        }

        super.onCreate(savedInstance);
    }
    //temporary

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        clearSubscriptions();
    }

    @Override
    public void addSubscription(Subscription s)
    {
        subscriptions.add(s);
    }

    @Override
    public void clearSubscriptions()
    {
        subscriptions.clear();
    }

    @Override
    public Context getContext()
    {
        return this;
    }
}
