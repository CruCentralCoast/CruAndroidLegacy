package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

public class BasicInfoFragment extends org.androidcru.crucentralcoast.presentation.views.ministryteams.BasicInfoFragment
{
    @Override
    public void onNext(FormHolder formHolder)
    {
        if(validator.validate())
        {
            // gets the validated information and overwrites the user's information in shared preferences on a background thread
            SharedPreferencesUtil.writeBasicInfo(nameField.getText().toString(), emailField.getText().toString(), phoneField.getText().toString());

            formHolder.next();
        }
    }
}
