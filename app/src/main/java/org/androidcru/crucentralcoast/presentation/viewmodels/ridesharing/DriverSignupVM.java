package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.telephony.PhoneNumberFormattingTextWatcher;
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

import butterknife.BindView;
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

    @BindView(R.id.name_field) @NotEmpty public EditText nameField;
    @BindView(R.id.phone_field) @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX, messageResId = R.string.phone_number_error) public EditText phoneField;

    @BindView(R.id.round_trip) RadioButton roundTrip;
    @BindView(R.id.to_event) RadioButton toEvent;
    @BindView(R.id.direction) RadioGroup directionGroup;

    @BindView(R.id.gender_field) @Select Spinner genderField;
    @BindView(R.id.gender_view) TextView genderView;

    @BindView(R.id.time_field) @NotEmpty EditText rideTime;
    @BindView(R.id.date_field) @NotEmpty EditText rideDate;

    @BindView(R.id.car_capacity_field) @NotEmpty @Min(value = 1) EditText carCapacity;

    @BindView(R.id.radius_field) @NotEmpty TextView radiusField;
    @BindView(com.google.android.gms.R.id.place_autocomplete_search_input) @NotEmpty EditText searchInput;

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

        nameField.setText(sharedPreferences.getString(AppConstants.USER_NAME, null));
        phoneField.setText(sharedPreferences.getString(AppConstants.USER_PHONE_NUMBER, null));
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
            String[] splitAddress = placeAddress.split(AppConstants.SPACE_COMMA_ESCAPE);
            String[] splitStateZip = splitAddress[2].split(" ");

            if (splitStateZip.length == 2)
            {
                location = new Location(splitStateZip[1], splitStateZip[0],
                        splitAddress[1], splitAddress[0], splitAddress[3],
                        new double[] {precisePlace.longitude, precisePlace.latitude});
            }
            else
            {
                location = new Location(null, null,
                        splitAddress[1], splitAddress[0], splitAddress[3],
                        new double[] {precisePlace.longitude, precisePlace.latitude});
            }
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

    protected void initMap(GoogleMap googleMap)
    {
        if (map == null) {
            map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(AppConstants.CALPOLY_LAT, AppConstants.CALPOLY_LNG), 14.0f));
        } else {
            Timber.d("Unable to display map....");
        }
    }

    public OnMapReadyCallback onMapReady() {
        return googleMap -> {
            initMap(googleMap);
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

    /*protected TextWatcher createCarCapacityWatcher()
    {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

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
    }*/
}
