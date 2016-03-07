package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.data.models.RideFilter;
import org.androidcru.crucentralcoast.data.models.queries.ConditionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.OptionsBuilder;
import org.androidcru.crucentralcoast.data.models.queries.Query;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import butterknife.Bind;
import butterknife.OnClick;

public class RideFilterVM extends BaseRideVM
{
    private RideFilter rideFilter;

    @Bind(R.id.round_trip) RadioButton roundTrip;
    @Bind(R.id.direction) RadioGroup directionGroup;
    @Bind(R.id.gender_field) @Select Spinner genderField;
    @Bind(R.id.time_field) @NotEmpty EditText rideTime;
    @Bind(R.id.date_field) @NotEmpty EditText rideDate;
    @Bind(com.google.android.gms.R.id.place_autocomplete_search_input) @NotEmpty EditText searchInput;


    private Ride.Direction[] directions;
    private String[] genders;

    public RideFilterVM(View rootView, FragmentManager fm)
    {
        super(rootView, fm);
        this.rideFilter = new RideFilter();
        generateDirections();
        bindUI();
    }

    private void generateDirections()
    {
        directions = Ride.Direction.values();
    }

    @Override
    protected void placeSelected(LatLng precisePlace, String placeAddress)
    {
        rideFilter.location = precisePlace;
    }

    public RideFilter getRideFilter()
    {
        int selectedRadioIndex = directionGroup.indexOfChild(rootView.findViewById(directionGroup.getCheckedRadioButtonId()));
        switch (selectedRadioIndex)
        {
            case 0:
                rideFilter.direction = Ride.Direction.TO;
                break;
            case 1:
                rideFilter.direction = Ride.Direction.ROUNDTRIP;
        }
        rideFilter.gender = (String) genderField.getSelectedItem();
        rideFilter.dateTime = ZonedDateTime.of(date, time, ZoneId.systemDefault());

        ZonedDateTime threeHoursAfter = rideFilter.dateTime.plusHours(3l);
        ZonedDateTime threeHoursBefore = rideFilter.dateTime.minusHours(3l);

        Query query = new Query.Builder()
                .setCondition(new ConditionsBuilder()
                        .setCombineOperator(ConditionsBuilder.OPERATOR.AND)
                        .addRestriction(new ConditionsBuilder()
                                .setField(Ride.serializedGender)
                                .addRestriction(ConditionsBuilder.OPERATOR.EQUALS, rideFilter.gender)
                                .build())
                        .addRestriction(new ConditionsBuilder()
                                .setField(Ride.serializedTime)
                                .addRestriction(ConditionsBuilder.OPERATOR.LTE, CruApplication.gson.toJsonTree(threeHoursAfter))
                                .addRestriction(ConditionsBuilder.OPERATOR.GTE, CruApplication.gson.toJsonTree(threeHoursBefore))
                                .build())
                        .addRestriction(new ConditionsBuilder()
                                .setField(Ride.serializedDirection)
                                .addRestriction(ConditionsBuilder.OPERATOR.EQUALS, rideFilter.direction.getValue())
                                .build())
                        .build())
                .setOptions(new OptionsBuilder()
                        .addOption(OptionsBuilder.OPTIONS.LIMIT, 5)
                        .build())
                .build();

        Logger.json(CruApplication.gson.toJson(query));

        return rideFilter;
    }

    private void bindUI()
    {
        roundTrip.setChecked(true);
        ViewUtil.setSpinner(genderField, gendersForSpinner(R.array.genders_filter), null, 0);
    }

    @OnClick(R.id.time_field)
    public void onTimeClicked(View v)
    {
        onEventTimeClicked(v);
    }

    @OnClick(R.id.date_field)
    public void onDateClicked(View v)
    {
        onEventDateClicked(v);
    }


}
