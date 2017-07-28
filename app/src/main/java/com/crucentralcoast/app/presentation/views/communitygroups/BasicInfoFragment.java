package com.crucentralcoast.app.presentation.views.communitygroups;

import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

public class BasicInfoFragment extends com.crucentralcoast.app.presentation.views.ministryteams.BasicInfoFragment
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
