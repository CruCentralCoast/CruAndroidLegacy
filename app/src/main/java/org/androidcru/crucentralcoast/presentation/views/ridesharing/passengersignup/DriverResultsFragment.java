package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

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
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;

public class DriverResultsFragment extends FormContentFragment
{

    private RecyclerView driverResultsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_driver_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        driverResultsList = (RecyclerView) view.findViewById(R.id.driver_results_list);
        driverResultsList.setLayoutManager(new LinearLayoutManager(getContext()));
        formHolder.setNavigationVisbility(View.GONE);
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
        driverResultsList.setAdapter(new DriverResultsAdapter(getActivity(), results));
    }
}
