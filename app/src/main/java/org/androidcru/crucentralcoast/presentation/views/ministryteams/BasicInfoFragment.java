package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BasicInfoFragment extends FormContentFragment
{
    SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();
    private BasicInfoValidator validator;

    @Bind(R.id.name_field) EditText nameField;
    @Bind(R.id.email_field) EditText emailField;
    @Bind(R.id.phone_field) EditText phoneField;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ministry_team_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        validator = new BasicInfoValidator(view);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    public void setupUI()
    {
        formHolder.setTitle("Contact Information");

        nameField.setText(sharedPreferences.getString(AppConstants.USER_NAME, null));
        phoneField.setText(sharedPreferences.getString(AppConstants.USER_PHONE_NUMBER, null));
        emailField.setText(sharedPreferences.getString(AppConstants.USER_EMAIL, null));
    }

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            // gets the validated information and overwrites the user's information in shared preferences on a background thread
            sharedPreferences.edit().putString(AppConstants.USER_NAME, nameField.getText().toString()).apply();
            sharedPreferences.edit().putString(AppConstants.USER_PHONE_NUMBER, phoneField.getText().toString()).apply();
            sharedPreferences.edit().putString(AppConstants.USER_EMAIL, emailField.getText().toString()).apply();


            super.onNext();
        }
    }
}
