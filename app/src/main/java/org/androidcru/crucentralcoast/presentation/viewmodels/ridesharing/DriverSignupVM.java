package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.Activity;
import android.app.FragmentManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.BindingAdapters;
import org.androidcru.crucentralcoast.util.DisplayMetricsUtil;
import org.androidcru.crucentralcoast.util.MathUtil;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class DriverSignupVM extends BaseRideVM
{
    private Ride ride;

    public Double radius;
    private GoogleMap map;
    private Marker marker;
    private Circle circle;
    private LatLng center;

    public boolean editing;
    protected Ride.Direction[] directions;

    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @Bind(R.id.gender_field) @Select Spinner genderField;
    @Bind(R.id.trip_type_field) @Select Spinner tripTypeField;
    @Bind(R.id.car_capacity_field) @Select Spinner carCapacity;
    @Bind(R.id.event_time_field) @NotEmpty EditText timeField;
    @Bind(R.id.event_date_field) @NotEmpty EditText dateField;
    @Bind(R.id.gender_view) TextView genderView;

    public DriverSignupVM(Activity activity, FragmentManager fm)
    {
        this(activity, fm, new Ride());
    }

    public DriverSignupVM(Activity activity, FragmentManager fm, Ride ride)
    {
        super(activity, fm);
        this.editing = ride != null && !ride.isEmpty();
        this.ride = ride != null ? ride : new Ride();

        generateDirections();
        bindUI();
    }

    private void bindUI()
    {
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        BindingAdapters.setSpinner(tripTypeField, directionsForSpinner(directions), null, getDirectionIndex(ride.direction, directions));
        BindingAdapters.setSpinner(carCapacity, carCapacityForSpinner(), null, getCarCapacityIndex(ride.carCapacity));
        if(editing)
        {
            genderField.setVisibility(View.GONE);
            genderView.setVisibility(View.VISIBLE);
            nameField.setText(ride.driverName);
            phoneField.setText(ride.driverNumber);

            timeField.setText(ride.time.toLocalTime().format(DateTimeFormatter.RFC_1123_DATE_TIME));
            dateField.setText(ride.time.toLocalDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        }
        else
        {
            BindingAdapters.setSpinner(genderField, gendersForSpinner(R.array.genders), null, getGenderIndex(ride.gender));
            timeField.setOnKeyListener(null);
            dateField.setOnKeyListener(null);
        }

    }

    private String[] carCapacityForSpinner()
    {
        int startCapacity = 1;
        if(editing)
        {
            startCapacity = ride.carCapacity;
        }
        String[] carCapacities = new String[12 - startCapacity + 2];
        carCapacities[0] = "Select Car Capacity";
        for(int i = 1; i < carCapacities.length; i++)
        {
            carCapacities[i] = String.valueOf(startCapacity + i - 1);
        }

        return carCapacities;
    }

    private int getCarCapacityIndex(int capacity)
    {
        int index = 0;
        String[] carCapacities = carCapacityForSpinner();
        for(int i = 0; i < carCapacities.length; i++)
        {
            if(String.valueOf(capacity).equals(carCapacities[i]))
            {
                index = i;
            }
        }

        return index;
    }

    private int retrieveCarCapacity()
    {
        return Integer.valueOf((String) carCapacity.getSelectedItem());
    }

    public Ride getRide()
    {
        ride.driverName = nameField.getText().toString();
        ride.driverNumber = phoneField.getText().toString();
        ride.gender = (String) genderField.getSelectedItem();
        ride.carCapacity = retrieveCarCapacity();
        ride.direction = retrieveDirection(tripTypeField, directions);
        ride.time = ZonedDateTime.of(date, time, ZoneId.systemDefault());
        return ride;
    }

    private void generateDirections()
    {
        if(editing)
        {
            ArrayList<Ride.Direction> directions = new ArrayList<>();
            switch(ride.direction)
            {
                case TO:
                    directions.add(Ride.Direction.TO);
                    directions.add(Ride.Direction.ROUNDTRIP);
                    break;
                case FROM:
                    directions.add(Ride.Direction.FROM);
                    directions.add(Ride.Direction.ROUNDTRIP);
                    break;
                case ROUNDTRIP:
                    directions.add(Ride.Direction.ROUNDTRIP);
            }
            this.directions = directions.toArray(new Ride.Direction[directions.size()]);
        }
        else
        {
            directions = new Ride.Direction[] {Ride.Direction.TO, Ride.Direction.FROM, Ride.Direction.ROUNDTRIP};
        }
    }

    @OnClick(R.id.event_time_field)
    public void onTimeClicked(View v)
    {
        onEventTimeClicked(v);
    }

    @OnClick(R.id.event_date_field)
    public void onDateClicked(View v)
    {
        onEventDateClicked(v);
    }

    @Override
    protected void placeSelected(LatLng precisePlace, String placeAddress)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(precisePlace, 14.0f));
        center = precisePlace;
        setMarker(center);

        if (radius != null)
            setCircle(center, radius);

        if(placeAddress != null)
        {
            String[] splitAddress = placeAddress.split("\\s*,\\s*");
            String[] splitStateZip = splitAddress[2].split(" ");
            ride.location = new Location(splitStateZip[1], splitStateZip[0],
                    splitAddress[1], splitAddress[0], splitAddress[3]);
            ride.location.preciseLocation = precisePlace;
        }
    }

    @OnTextChanged(R.id.radius_field)
    public void onRadiusChanged(CharSequence s)
    {
        if(!s.toString().isEmpty())
        {
            try
            {
                this.radius = Double.valueOf(s.toString());
                setCircle(center, this.radius);
                ride.radius = radius;
            }
            catch (NumberFormatException e)
            {}
        }
    }

    public OnMapReadyCallback onMapReady()
    {
        return googleMap -> {
            if (map == null)
            {
                map = googleMap;
                if(ride.address != null)
                    placeSelected(new LatLng(ride.address.getLatitude(), ride.address.getLongitude()), null);
                else
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(AppConstants.CALPOLY_LAT, AppConstants.CALPOLY_LNG), 14.0f));
            }
            else
            {
                Logger.d("Unable to display map....");
            }
        };
    }

    private void setMarker(LatLng latLng)
    {
        if (marker != null) {
            marker.remove();
        }

        marker = map.addMarker(new MarkerOptions()
                .position(latLng));
    }

    private void setCircle(LatLng center, double radius)
    {
        if(circle != null)
            circle.remove();

        if(center != null)
        {
            double radiusMeters = MathUtil.convertMilesToMeters(radius);
            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radiusMeters));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(center);
            builder.include(new LatLng(MathUtil.addMetersToLatitude(center.latitude, radiusMeters), center.longitude));
            builder.include(new LatLng(MathUtil.addMetersToLatitude(center.latitude, -radiusMeters), center.longitude));
            LatLngBounds bounds = builder.build();

            int padding = DisplayMetricsUtil.dpToPx(CruApplication.getContext(), 8);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu);
        }
    }
}
