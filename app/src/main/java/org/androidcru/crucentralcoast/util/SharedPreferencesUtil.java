package org.androidcru.crucentralcoast.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.ConnectionResult;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;

public class SharedPreferencesUtil
{
    private static SharedPreferences getSharedPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void writeBasicInfo(Context context, String name, String email, String phoneNumber)
    {
        SharedPreferences preferences = getSharedPreferences(context);
        if (name != null)
            preferences.edit().putString(AppConstants.USER_NAME, name).apply();
        if (email != null)
            preferences.edit().putString(AppConstants.USER_EMAIL, email).apply();
        if (phoneNumber != null)
            preferences.edit().putString(AppConstants.USER_PHONE_NUMBER, phoneNumber).apply();
    }

    public static void writeGCMID(Context context, String key)
    {
        getSharedPreferences(context).edit().putString(context.getString(R.string.gcm_registration_id), key).apply();
    }

    public static void writeSentTokenToServer(Context context, Boolean value)
    {
        getSharedPreferences(context).edit().putBoolean(AppConstants.SENT_TOKEN_TO_SERVER, value).apply();
    }

    public static void writeCalendarID(Context context, String cruEventId, long eventId)
    {
        getSharedPreferences(context).edit().putLong(cruEventId, eventId).commit();
    }

    public static void writeFBAccessToken(Context context, String fbToken)
    {
        getSharedPreferences(context).edit().putString(AppConstants.FB_TOKEN_KEY, fbToken).apply();
    }

    public static void writeMinistrySubscriptionIsSubscribed(Context context, String ministrySubscriptionId, Boolean isSubscribed)
    {
        getSharedPreferences(context).edit().putBoolean(ministrySubscriptionId, isSubscribed).commit();
    }

    public static void writeFirstLaunch(Context context, Boolean firstLaunch)
    {
        getSharedPreferences(context).edit().putBoolean(AppConstants.FIRST_LAUNCH, firstLaunch).apply();
    }

    public static void removeFBTokenKey(Context context)
    {
        getSharedPreferences(context).edit().remove(AppConstants.FB_TOKEN_KEY).apply();
    }

    public static void removeKey(Context context, String key)
    {
        getSharedPreferences(context).edit().remove(key).commit();
    }

    public static void writePlayServicesCode(Context context, int code)
    {
        getSharedPreferences(context).edit().putInt(AppConstants.PLAY_SERVICES, code).apply();
    }

    public static int getPlayServicesCode(Context context)
    {
        return getSharedPreferences(context).getInt(AppConstants.PLAY_SERVICES, ConnectionResult.SUCCESS);
    }

    public static Boolean containsKey(Context context, String key)
    {
        return getSharedPreferences(context).contains(key);
    }

    public static void writeLoginInformation(Context context, String loginName, String leaderApiKey)
    {
        getSharedPreferences(context).edit().putString(AppConstants.USERNAME_KEY, loginName).commit();
        getSharedPreferences(context).edit().putString(AppConstants.LOGIN_KEY, leaderApiKey).commit();
    }

    public static String getLoginUsername(Context context)
    {
        return getSharedPreferences(context).getString(AppConstants.USERNAME_KEY, "");
    }

    public static String getUserName(Context context)
    {
        return getSharedPreferences(context).getString(AppConstants.USER_NAME, null);
    }

    public static String getUserEmail(Context context)
    {
        return getSharedPreferences(context).getString(AppConstants.USER_EMAIL, null);
    }

    public static String getUserPhoneNumber(Context context)
    {
        return getSharedPreferences(context).getString(AppConstants.USER_PHONE_NUMBER, null);
    }

    public static String getGCMID(Context context)
    {
        return getSharedPreferences(context).getString(context.getString(R.string.gcm_registration_id), "");
    }

    public static Boolean getMinistrySubscriptionIsSubscribed(Context context, String ministrySubscriptionId)
    {
        return getSharedPreferences(context).getBoolean(ministrySubscriptionId, false);
    }

    public static long getCalendarEventId(Context context, String cruEventId)
    {
        return getSharedPreferences(context).getLong(cruEventId, -1);
    }

    public static Boolean isFirstLaunch(Context context)
    {
        return getSharedPreferences(context).getBoolean(AppConstants.FIRST_LAUNCH, false);
    }
}
