package com.crucentralcoast.app.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.CruApplication;
import com.google.android.gms.common.ConnectionResult;

public class SharedPreferencesUtil
{
    private static SharedPreferences sharedPreferences = null;

    private static SharedPreferences getSharedPreferences()
    {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CruApplication.getContext());
        return sharedPreferences;
    }

    public static void writeBasicInfo(String name, String email, String phoneNumber)
    {
        SharedPreferences preferences = getSharedPreferences();
        if (name != null)
            preferences.edit().putString(AppConstants.USER_NAME, name).apply();
        if (email != null)
            preferences.edit().putString(AppConstants.USER_EMAIL, email).apply();
        if (phoneNumber != null)
            preferences.edit().putString(AppConstants.USER_PHONE_NUMBER, phoneNumber).apply();
    }

    public static void writeFCMID(String key)
    {
        getSharedPreferences().edit().putString(AppConstants.FCM_REGISTRATION_ID, key).apply();
    }

    public static void writeSentTokenToServer(Boolean value)
    {
        getSharedPreferences().edit().putBoolean(AppConstants.SENT_TOKEN_TO_SERVER, value).apply();
    }

    public static void writeCalendarID(String cruEventId, long eventId)
    {
        getSharedPreferences().edit().putLong(cruEventId, eventId).commit();
    }

    public static void writeFBAccessToken(String fbToken)
    {
        getSharedPreferences().edit().putString(AppConstants.FB_TOKEN_KEY, fbToken).apply();
    }

    public static void writeMinistrySubscriptionIsSubscribed(String ministrySubscriptionId, Boolean isSubscribed)
    {
        getSharedPreferences().edit().putBoolean(ministrySubscriptionId, isSubscribed).commit();
    }

    public static void writeFirstLaunch(Boolean firstLaunch)
    {
        getSharedPreferences().edit().putBoolean(AppConstants.FIRST_LAUNCH, firstLaunch).apply();
    }

    public static void removeFBTokenKey()
    {
        getSharedPreferences().edit().remove(AppConstants.FB_TOKEN_KEY).apply();
    }

    public static void removeKey(String key)
    {
        getSharedPreferences().edit().remove(key).commit();
    }

    public static void writePlayServicesCode(int code)
    {
        getSharedPreferences().edit().putInt(AppConstants.PLAY_SERVICES, code).apply();
    }

    public static int getPlayServicesCode()
    {
        return getSharedPreferences().getInt(AppConstants.PLAY_SERVICES, ConnectionResult.SUCCESS);
    }

    public static Boolean containsKey(String key)
    {
        return getSharedPreferences().contains(key);
    }

    public static void writeLoginInformation(String loginName, String leaderApiKey)
    {
        getSharedPreferences().edit().putString(AppConstants.USERNAME_KEY, loginName).commit();
        getSharedPreferences().edit().putString(AppConstants.LOGIN_KEY, leaderApiKey).commit();
    }

    public static String getLeaderAPIKey()
    {
        return getSharedPreferences().getString(AppConstants.LOGIN_KEY, "");
    }

    public static String getLoginUsername()
    {
        return getSharedPreferences().getString(AppConstants.USERNAME_KEY, "");
    }

    public static String getUserName()
    {
        return getSharedPreferences().getString(AppConstants.USER_NAME, null);
    }

    public static String getUserEmail()
    {
        return getSharedPreferences().getString(AppConstants.USER_EMAIL, null);
    }

    public static String getUserPhoneNumber()
    {
        return getSharedPreferences().getString(AppConstants.USER_PHONE_NUMBER, null);
    }

    public static String getFCMID()
    {
        return getSharedPreferences().getString(AppConstants.FCM_REGISTRATION_ID, "");
    }

    public static Boolean getMinistrySubscriptionIsSubscribed(String ministrySubscriptionId)
    {
        return getSharedPreferences().getBoolean(ministrySubscriptionId, false);
    }

    public static long getCalendarEventId(String cruEventId)
    {
        return getSharedPreferences().getLong(cruEventId, -1);
    }

    public static Boolean isFirstLaunch()
    {
        return getSharedPreferences().getBoolean(AppConstants.FIRST_LAUNCH, false);
    }

    public static boolean getNotificationEnabled()
    {
        return getSharedPreferences().getBoolean(AppConstants.NOTIFICATION_KEY, true);
    }

    public static void setNotificationEnable(boolean value)
    {
        getSharedPreferences().edit().putBoolean(AppConstants.NOTIFICATION_KEY, value).commit();
    }

    public static boolean getAuthorizedDriver(String number)
    {
        return getSharedPreferences().getString(AppConstants.AUTHORIZED_KEY, "").equals(number);
    }

    public static void setAuthoriziedDriver(String number)
    {
        getSharedPreferences().edit().putString(AppConstants.AUTHORIZED_KEY, number).commit();
    }

    public static void setMinistryTeamSignup(String ministryName)
    {
        getSharedPreferences().edit().putBoolean(ministryName, true).commit();
    }

    public static boolean getMinistryTeamSignup(String ministryName)
    {
        return getSharedPreferences().getBoolean(ministryName, false);
    }
}
