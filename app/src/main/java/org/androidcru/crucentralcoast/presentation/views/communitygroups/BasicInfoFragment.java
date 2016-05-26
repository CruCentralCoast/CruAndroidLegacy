package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

public class BasicInfoFragment extends org.androidcru.crucentralcoast.presentation.views.ministryteams.BasicInfoFragment
{
    @Override
    public void onNext(FormHolder formHolder)
    {
        if(validator.validate())
        {
            // gets the validated information and overwrites the user's information in shared preferences on a background thread
            sharedPreferences.edit().putString(AppConstants.USER_NAME, nameField.getText().toString()).apply();
            sharedPreferences.edit().putString(AppConstants.USER_PHONE_NUMBER, phoneField.getText().toString()).apply();
            sharedPreferences.edit().putString(AppConstants.USER_EMAIL, emailField.getText().toString()).apply();

            formHolder.next();
        }
    }
}
