package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruName;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.data.providers.MinistryTeamProvider;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.observers.Observers;


public class BasicInfoFragment extends FormContentFragment
{
    SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();
    private BaseValidator validator;

    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @Bind(R.id.email_field) @Email EditText emailField;


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
        validator = new BaseValidator(this);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        formHolder.setTitle("Contact Information");

        // Autofills the form if the information is available
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

            // gets back the ministry team object from the form holder.
            MinistryTeam ministryTeam = (MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM);
            MinistryTeamProvider.joinMinistryTeam(Observers.empty(), ministryTeam.id, new CruUser(new CruName(nameField.getText().toString(), ""), emailField.getText().toString(), phoneField.getText().toString()));

            super.onNext();
        }
    }
}
