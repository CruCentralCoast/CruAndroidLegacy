package com.crucentralcoast.app.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruName;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.data.providers.MinistryTeamProvider;
import com.crucentralcoast.app.presentation.validator.BaseValidator;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.presentation.views.forms.FormContentFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;
import com.crucentralcoast.app.util.AutoFill;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

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

        AutoFill.setupAutoFillData((BaseAppCompatActivity)getActivity(), () -> {
            // Autofills the form if the information is available
            nameField.setText(SharedPreferencesUtil.getUserName());
            phoneField.setText(SharedPreferencesUtil.getUserPhoneNumber());
            emailField.setText(SharedPreferencesUtil.getUserEmail());
        });
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

            // save signup for this minstry team to avoid having to enter info again
            SharedPreferencesUtil.setMinistryTeamSignup(((MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM)).name);

            // gets back the ministry team object from the form holder.
            MinistryTeam ministryTeam = (MinistryTeam) formHolder.getDataObject(JoinMinistryTeamActivity.MINISTRY_TEAM);
            MinistryTeamProvider.joinMinistryTeam(Observers.empty(), ministryTeam.id, new CruUser(new CruName(nameField.getText().toString(), ""), emailField.getText().toString(), phoneField.getText().toString()));

            super.onNext(formHolder);
        }
    }
}
