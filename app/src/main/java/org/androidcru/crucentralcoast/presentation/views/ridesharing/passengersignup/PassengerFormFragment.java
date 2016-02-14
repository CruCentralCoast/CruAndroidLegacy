package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.databinding.FragmentPassengerFormBinding;

public class PassengerFormFragment extends Fragment
{

    private FragmentPassengerFormBinding binding;
    private PassengerFormValidator validator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_passenger_form, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        validator = new PassengerFormValidator(binding);

        binding.fab.setOnClickListener();
    }
}
