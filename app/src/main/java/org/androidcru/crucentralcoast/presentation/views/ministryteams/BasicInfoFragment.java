package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BasicInfoFragment extends FormContentFragment
{
    private BasicInfoValidator validator;

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
    }

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            super.onNext();
        }
    }
}
