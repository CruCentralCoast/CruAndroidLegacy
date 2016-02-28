package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

public class BasicInfoFragment extends FormContentFragment
{
    private BasicInfoValidator validator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ministry_team_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validator = new BasicInfoValidator(view);
    }

    @Override
    public void setupUI() {}

    @Override
    public void onNext()
    {
        if(validator.validate())
        {
            super.onNext();
        }
    }
}
