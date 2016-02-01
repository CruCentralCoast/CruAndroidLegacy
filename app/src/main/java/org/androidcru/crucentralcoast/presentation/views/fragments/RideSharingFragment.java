package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.activities.DriverSignupActivity;
import org.androidcru.crucentralcoast.presentation.views.activities.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.adapters.RideSharingAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RideSharingFragment extends Fragment
{
    @Bind(R.id.launchDriver) Button driverButton;
    @Bind(R.id.launchPassenger) Button passengerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_ridesharing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DriverSignupActivity.class));
            }
        });
    }
}
