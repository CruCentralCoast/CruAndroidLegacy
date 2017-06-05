package com.crucentralcoast.app.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.Location;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.queries.ConditionsBuilder;
import com.crucentralcoast.app.data.models.queries.OptionsBuilder;
import com.crucentralcoast.app.data.models.queries.Query;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;
import com.crucentralcoast.app.presentation.views.ridesharing.passengersignup.RideInfoFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import timber.log.Timber;

public class RideFilterVM extends BaseRideVM {
    public Location location;
    private CruEvent event;
    public int genderSelected = 0;
    private int directionSelected = Ride.Direction.ROUNDTRIP.ordinal();

    @BindView(R.id.round_trip)
    RadioButton roundTrip;
    @BindView(R.id.direction)
    RadioGroup directionGroup;
    @BindView(R.id.gender_field)
    Spinner genderField;
    @BindView(R.id.time_field)
    @NotEmpty
    EditText rideTime;
    @BindView(R.id.date_field)
    @NotEmpty
    EditText rideDate;

    public RideFilterVM(RideInfoFragment fragment, FragmentManager fm, CruEvent event) {
        super(fragment, fm);
        this.event = event;
        eventEndDate = DateTimeUtils.toGregorianCalendar(event.endDate);
        bindUI(fragment);
    }

    @Override
    protected void placeSelected(LatLng precisePlace, String placeAddress) {
        if (placeAddress != null) {
            location = new Location(placeAddress, new double[]{precisePlace.longitude, precisePlace.latitude});
        }
    }

    public Pair<Query, Ride.Direction> getQuery() {
        //ride direction
        int selectedRadioIndex = directionGroup.indexOfChild(rootView.findViewById(directionGroup.getCheckedRadioButtonId()));
        Ride.Direction direction = Ride.Direction.ROUNDTRIP;
        switch (selectedRadioIndex) {
            case 0:
                direction = Ride.Direction.TO;
                break;
            case 1:
                direction = Ride.Direction.ROUNDTRIP;
                break;
        }
        //ride gender
        String gender = (String) genderField.getSelectedItem();
        int genderId = -1;

        if (!gender.equals(context.getString(R.string.any_gender)))
            genderId = Ride.Gender.getFromColloquial((String) genderField.getSelectedItem()).getId();

        //conditions
        ConditionsBuilder conditions = new ConditionsBuilder()
                .setCombineOperator(ConditionsBuilder.OPERATOR.AND)
                .addRestriction(new ConditionsBuilder()
                        .setField(Ride.sEvent)
                        .addRestriction(ConditionsBuilder.OPERATOR.EQUALS, event.id))
                .addRestriction(new ConditionsBuilder()
                        .setField(Ride.sDirection)
                        .addRestriction(ConditionsBuilder.OPERATOR.EQUALS, direction.getValue()));

        //don't include gender if it was "Any"
        if (genderId > -1) {
            conditions.addRestriction(new ConditionsBuilder()
                    .setField(Ride.sGender)
                    .addRestriction(ConditionsBuilder.OPERATOR.EQUALS, genderId));
        }

        //build query
        Query query = new Query.Builder()
                .setCondition(conditions.build())
                .setOptions(new OptionsBuilder()
                        .addOption(OptionsBuilder.OPTIONS.LIMIT, 5)
                        .build())
                .build();

        Timber.d(CruApplication.gson.toJson(query));

        return new Pair<>(query, direction);
    }

    public ZonedDateTime getDateTime() {
        return ZonedDateTime.of(rideSetDate, rideSetTime, ZoneId.systemDefault());
    }

    @Override
    protected String[] gendersForSpinner() {
        String[] genders = super.gendersForSpinner();
        genders[0] = context.getString(R.string.any_gender);
        return genders;
    }

    public void bindUI(BaseSupportFragment fragment) {
        rebind(fragment);
        directionGroup.check(directionSelected == Ride.Direction.TO.ordinal() ? R.id.to_event : R.id.round_trip);
        ViewUtil.setSpinner(genderField, gendersForSpinner(), null, genderSelected);
    }

    @OnClick(R.id.time_field)
    public void onTimeClicked(View v) {
        onEventTimeClicked(v, org.threeten.bp.DateTimeUtils.toGregorianCalendar(event.startDate));
    }

    @OnClick(R.id.date_field)
    public void onDateClicked(View v) {
        onEventDateClicked(v, org.threeten.bp.DateTimeUtils.toGregorianCalendar(event.startDate));
    }

    @OnItemSelected(R.id.gender_field)
    public void onGenderSelected(int position) {
        genderSelected = position;
    }

    public Ride.Gender getGender() {
        return Ride.Gender.getFromColloquial((String) genderField.getSelectedItem());
    }

    @OnClick({R.id.to_event, R.id.round_trip})
    public void onDirectionSelected(RadioButton button) {
        switch (button.getId()) {
            case R.id.to_event:
                directionSelected = Ride.Direction.TO.ordinal();
                break;
            case R.id.round_trip:
                directionSelected = Ride.Direction.ROUNDTRIP.ordinal();
                break;
        }
    }
}
