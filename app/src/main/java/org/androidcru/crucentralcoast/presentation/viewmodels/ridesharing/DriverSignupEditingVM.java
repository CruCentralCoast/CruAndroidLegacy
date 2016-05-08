package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mobsandgeeks.saripaar.QuickRule;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.GregorianCalendar;

import butterknife.OnClick;

public class DriverSignupEditingVM extends DriverSignupVM {

    private Ride ride;
    protected int minCapacity;

    public DriverSignupEditingVM(BaseAppCompatActivity activity, FragmentManager fm, Ride ride, ZonedDateTime eventEndTime) {
        super(activity, fm, ride.eventId, eventEndTime);

        this.ride = ride;
        //set time variables in the parent class
        rideSetDate = this.ride.time.toLocalDate();
        rideSetTime = this.ride.time.toLocalTime();

        bindUI();
    }

    @Override
    protected void bindUI() {
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        genderField.setVisibility(View.GONE);
        genderView.setVisibility(View.VISIBLE);
        genderView.setText(ride.gender.getColloquial());

        carCapacity.setText(Integer.toString(ride.carCapacity));
        minCapacity = ride.passengerIds.size();

        nameField.setText(ride.driverName);
        phoneField.setText(ride.driverNumber);
        rideTime.setText(ride.time.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
        rideDate.setText(ride.time.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

        searchInput.setText(ride.location.toString());
        radiusField.setText(Double.toString(ride.radius));

        switch (ride.direction)
        {
            case TO:
                directionGroup.check(toEvent.getId());
                break;
            default:
                directionGroup.check(roundTrip.getId());
        }

        validator.validator.put(carCapacity, new QuickRule<EditText>()
        {
            @Override
            public boolean isValid(EditText view)
            {
                try
                {
                    int i = Integer.valueOf(view.getText().toString());
                    return i >= ride.carCapacity && i <= AppConstants.MAX_CAR_CAPACITY;
                }
                catch (NumberFormatException e)
                {
                    return false;
                }
            }

            @Override
            public String getMessage(Context context)
            {
                return context.getString(R.string.car_capacity_error);
            }
        });
    }

    @Override
    @OnClick(R.id.time_field)
    public void onTimeClicked(View v) {
        GregorianCalendar gc = DateTimeUtils.toGregorianCalendar(ride.time);
        onEventTimeClicked(v, gc);
    }

    @Override
    @OnClick(R.id.date_field)
    public void onDateClicked(View v) {
        GregorianCalendar gc = DateTimeUtils.toGregorianCalendar(ride.time);
        onEventDateClicked(v, gc);
    }

    @Override
    public OnMapReadyCallback onMapReady()
    {
        return googleMap -> {
            initMap(googleMap);
            if (ride != null && ride.location != null)
                updateMap(new LatLng(ride.location.geo[1], ride.location.geo[0]));
        };
    }
}
