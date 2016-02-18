package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.RideFilter;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.providers.GeocodeProvider;
import org.androidcru.crucentralcoast.presentation.util.DateTimeUtils;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DriverResultsFragment extends FormContentFragment
{

    private RecyclerView driverResultsList;
    private LinearLayout emptyList;
    private ProgressBar progressBar;

    private RideFilter filter;
    private List<Ride> results;

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
        emptyList = (LinearLayout) view.findViewById(R.id.empty_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @Override
    public void setupUI()
    {
        filter = (RideFilter) formHolder.getDataObject();

        formHolder.setNavigationVisibility(View.VISIBLE);
        formHolder.setNextVisibility(View.GONE);
        formHolder.setPreviousVisibility(View.VISIBLE);
        getRides();
    }

    private void getRides()
    {
        progressBar.setVisibility(View.VISIBLE);
        driverResultsList.setVisibility(View.GONE);
        emptyList.setVisibility(View.GONE);
        //next block might be red, AS is confused but it compiles (I'm too complicated for it)
        RideProvider.getInstance().requestRides()
                .subscribeOn(Schedulers.computation())
                .flatMap(rides -> Observable.from(rides).subscribeOn(Schedulers.computation()))
                .map(ride -> {
                    GeocodeProvider.getLatLng(getContext(), ride.location.toString())
                            .toBlocking()
                            .subscribe(address -> {
                                ride.location.preciseLocation = new LatLng(address.getLatitude(), address.getLongitude());
                            });
                    return ride;
                })
                //filter by gender
                //TODO should be more complex than this
                .filter(ride -> ride.gender.equals(filter.gender))
                //filter by location
                .filter(ride -> {
                    float[] results = new float[1];
                    Logger.d(Boolean.toString(ride.location.preciseLocation != null));
                    Location.distanceBetween(ride.location.preciseLocation.latitude, ride.location.preciseLocation.longitude,
                            filter.location.latitude, filter.location.longitude, results);
                    return results[0] <= ride.radius;
                })
                //filter by toEventDateTime
                .filter(ride -> {
                    if (filter.direction == Ride.Direction.TO || filter.direction == Ride.Direction.ROUNDTRIP)
                    {
                        return DateTimeUtils.within(ride.time, filter.toDateTime, 0, 3);
                    }
                    return true;
                })
                        //filter by fromEventDateTime
                //TODO update properly, fromTime
                .filter(ride -> {
                    if (filter.direction == Ride.Direction.FROM || filter.direction == Ride.Direction.ROUNDTRIP)
                    {
                        return DateTimeUtils.within(ride.time, filter.toDateTime, 0, 3);
                    }
                    return true;
                })
                //TODO filter by roundtripEventDateTime
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(results -> {
                    handleResults(results);
                }, e -> {}, () -> {
                    progressBar.setVisibility(View.GONE);
                    if(results == null || results.isEmpty())
                    {
                        driverResultsList.setVisibility(View.GONE);
                        emptyList.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        driverResultsList.setVisibility(View.VISIBLE);
                        emptyList.setVisibility(View.GONE);
                    }
                });
    }

    private void handleResults(List<Ride> results)
    {
        this.results = results;
        driverResultsList.setAdapter(new DriverResultsAdapter(this, formHolder, results));
    }
}
