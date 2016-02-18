package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.databinding.FragmentMinistryTeamFormBasicInfoBinding;
import org.androidcru.crucentralcoast.databinding.PassengerFormBasicInfoBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.PassengerVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

public class BasicInfoFragment extends FormContentFragment
{
    private FragmentMinistryTeamFormBasicInfoBinding binding;
    private BasicInfoValidator validator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentMinistryTeamFormBasicInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validator = new BasicInfoValidator(binding);
    }

    @Override
    public void setupUI()
    {

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
