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
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.observers.Observers;


public class BasicInfoFragment extends FormContentFragment
{
    protected BaseValidator validator;

    public @BindView(R.id.name_field) @NotEmpty EditText nameField;
    public @BindView(R.id.phone_field) @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    public @BindView(R.id.email_field) @Email EditText emailField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ministry_team_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        validator = new BaseValidator(this);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Autofills the form if the information is available
        nameField.setText(SharedPreferencesUtil.getUserName());
        phoneField.setText(SharedPreferencesUtil.getUserPhoneNumber());
        emailField.setText(SharedPreferencesUtil.getUserEmail());
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        formHolder.setTitle("Contact Information");
        formHolder.setSubtitle("");
    }

    @Override
    public void onNext(FormHolder formHolder)
    {
        if(validator.validate())
        {
            // gets the validated information and overwrites the user's information in shared preferences on a background thread
            SharedPreferencesUtil.writeBasicInfo(nameField.getText().toString(), emailField.getText().toString(), phoneField.getText().toString());


            // gets back the ministry team object from the form holder.
            MinistryTeam ministryTeam = (MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM);
            MinistryTeamProvider.joinMinistryTeam(Observers.empty(), ministryTeam.id, new CruUser(new CruName(nameField.getText().toString(), ""), emailField.getText().toString(), phoneField.getText().toString()));

            super.onNext(formHolder);
        }
    }
}
