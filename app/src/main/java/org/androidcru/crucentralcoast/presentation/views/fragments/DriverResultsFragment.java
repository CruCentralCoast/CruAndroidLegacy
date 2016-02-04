package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.views.adapters.DriverResultsAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class DriverResultsFragment extends ProvableFragment
{

    @Bind(R.id.driverresults_list) RecyclerView mDriverResultsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_driverresults, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mDriverResultsList.setLayoutManager(new LinearLayoutManager(getContext()));

        getRides();
    }

    private void getRides()
    {
        RideProvider.getInstance().requestRides()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                            handleResults(results);
                        }
                );
    }

    private void handleResults(ArrayList<Ride> results)
    {
        mDriverResultsList.setAdapter(new DriverResultsAdapter(getActivity(), results));
    }

    @Override
    public boolean validate()
    {
        return true;
    }
}
