package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.util.DisplayMetricsUtil;
import org.androidcru.crucentralcoast.util.MathUtil;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

public class DriverSignupVM extends BaseRideVM {
    SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();

    public Double radius;
    protected GoogleMap map;
    protected Marker marker;
    protected Circle circle;
    protected LatLng center;

    protected GregorianCalendar eventStartDateTime;
    protected int minCapacity;

    @Bind(R.id.name_field) @NotEmpty public EditText nameField;
    @Bind(R.id.phone_field) @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX, messageResId = R.string.phone_number_error) public EditText phoneField;

    @Bind(R.id.round_trip) RadioButton roundTrip;
    @Bind(R.id.to_event) RadioButton toEvent;
    @Bind(R.id.direction) RadioGroup directionGroup;

    @Bind(R.id.gender_field) @Select Spinner genderField;
    @Bind(R.id.gender_view) TextView genderView;

    @Bind(R.id.time_field) @NotEmpty EditText rideTime;
    @Bind(R.id.date_field) @NotEmpty EditText rideDate;

    @Bind(R.id.car_capacity_field) @NotEmpty @Min(value = 1) EditText carCapacity;

    @Bind(R.id.radius_field) @NotEmpty TextView radiusField;
    @Bind(com.google.android.gms.R.id.place_autocomplete_search_input) @NotEmpty EditText searchInput;

    private String eventId;
    private Location location;

    public DriverSignupVM(BaseAppCompatActivity activity, FragmentManager fm, String eventId, ZonedDateTime eventEndTime) {
        super(activity, fm);
        this.eventId = eventId;
        eventEndDate = DateTimeUtils.toGregorianCalendar(eventEndTime);
    }

    public DriverSignupVM(BaseAppCompatActivity activity, FragmentManager fm, String eventId, ZonedDateTime eventStartTime, ZonedDateTime eventEndTime) {
        this(activity, fm, eventId, eventEndTime);

        eventStartDateTime = DateTimeUtils.toGregorianCalendar(eventStartTime);
        bindUI();
    }

    protected void bindUI() {
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        ViewUtil.setSpinner(genderField, gendersForSpinner(), null, 0);
        directionGroup.check(roundTrip.getId());
        rideTime.setOnKeyListener(null);
        rideDate.setOnKeyListener(null);
        minCapacity = 0;

        nameField.setText(sharedPreferences.getString(AppConstants.USER_NAME, null));
        phoneField.setText(sharedPreferences.getString(AppConstants.USER_PHONE_NUMBER, null));
        carCapacity.addTextChangedListener(createCarCapacityWatcher());
    }

    protected int retrieveCarCapacity() {
        return Integer.parseInt(carCapacity.getText().toString());
    }


    protected Ride.Direction retrieveDirection(RadioGroup radioGroup)
    {
        int selectedRadioIndex = radioGroup.indexOfChild(rootView.findViewById(radioGroup.getCheckedRadioButtonId()));
        Ride.Direction direction = Ride.Direction.ROUNDTRIP;
        switch (selectedRadioIndex)
        {
            case 0:
                direction = Ride.Direction.TO;
                break;
            case 1:
                direction = Ride.Direction.ROUNDTRIP;
                break;
        }
        return direction;
    }
    //populates ride's fields with data from the view
    public Ride getRide() {
        return new Ride(nameField.getText().toString(), phoneField.getText().toString(),
                Ride.Gender.getFromColloquial((String) genderField.getSelectedItem()), eventId,
                ZonedDateTime.of(rideSetDate, rideSetTime, ZoneId.systemDefault()), location,
                radius, retrieveDirection(directionGroup), CruApplication.getGCMID(), retrieveCarCapacity());
    }

    @OnClick(R.id.time_field)
    public void onTimeClicked(View v) {
        onEventTimeClicked(v, eventStartDateTime);
    }

    @OnClick(R.id.date_field)
    public void onDateClicked(View v) {
        onEventDateClicked(v, eventStartDateTime);
    }

    @Override
    protected void placeSelected(LatLng precisePlace, String placeAddress) {
        updateMap(precisePlace);

        if (placeAddress != null) {
            location = new Location(placeAddress, new double[] {precisePlace.longitude, precisePlace.latitude});
        }
    }

    //updates the map on the bottom
    protected void updateMap(LatLng precisePlace) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(precisePlace, 14.0f));
        center = precisePlace;
        setMarker(center);

        if (radius != null)
            setCircle(center, radius);
    }

    @OnTextChanged(R.id.radius_field)
    public void onRadiusChanged(CharSequence s) {
        if (!s.toString().isEmpty()) {
            try {
                double newRad = Double.parseDouble(s.toString());
                if (newRad > AppConstants.MAX_RADIUS) {
                    newRad = AppConstants.MAX_RADIUS;
                    radiusField.setText("" + newRad);
                }
                this.radius = newRad;
                setCircle(center, this.radius);
            } catch (NumberFormatException e) {
                radiusField.setText("");
            }
        }
    }

    public OnMapReadyCallback onMapReady() {
        return googleMap -> {
            if (map == null) {
                map = googleMap;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(AppConstants.CALPOLY_LAT, AppConstants.CALPOLY_LNG), 14.0f));
            } else {
                Timber.d("Unable to display map....");
            }
        };
    }

    protected void setMarker(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }

        marker = map.addMarker(new MarkerOptions()
                .position(latLng));
    }

    protected void setCircle(LatLng center, double radius) {
        if (circle != null)
            circle.remove();

        if (center != null) {
            double radiusMeters = MathUtil.convertMilesToMeters(radius);
            circle = map.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radiusMeters)
                    .strokeWidth(AppConstants.RADIUS_STROKE_WID)
                    .strokeColor(R.color.cruGray));

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

    protected TextWatcher createCarCapacityWatcher()
    {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @OnTextChanged(R.id.car_capacity_field)
            @Override
            public void afterTextChanged(Editable s) {
                try
                {
                    if (s == null || s.toString().equals("")) {
                        return;
                    }

                    //make sure is within bounds
                    int setTo = Integer.parseInt(s.toString());
                    if (setTo < minCapacity)
                    {
                        carCapacity.setText(Integer.toString(minCapacity));
                    }
                    else if (setTo > AppConstants.MAX_CAR_CAPACITY)
                    {
                        carCapacity.setText(Integer.toString(AppConstants.MAX_CAR_CAPACITY));
                    }
                }
                catch (NumberFormatException nfe)
                {
                    carCapacity.setText("");
                }
            }
        };
    }
}
