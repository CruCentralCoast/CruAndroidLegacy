package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
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
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.presentation.util.MathUtil;
import org.androidcru.crucentralcoast.presentation.util.MetricsUtil;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class RideVM extends BaseRideVM
{
    public Ride ride;
    public Double radius;

    private GoogleMap map;
    private Marker marker;
    private Circle circle;
    private LatLng center;

    private boolean editing; //ride is being editted
    private static final double CALPOLY_LAT = 35.30021;
    private static final double CALPOLY_LNG = -120.66310;


    public RideVM(FragmentManager fm, Ride ride)
    {
        super(fm);
        this.ride = ride;
        direction = new ObservableField<>(null);
        editing = false;
    }

    public RideVM(FragmentManager fm, Ride ride, boolean editing)
    {
        super(fm);
        this.ride = ride;
        direction = new ObservableField<>(null);
        editing = true;

        if (editing) {
            populateBinds();
        }
    }

    @Override
    protected void syncFromDate(LocalDate date)
    {
        //TODO
    }

    @Override
    protected void syncFromTime(LocalTime time)
    {
        //TODO
    }

    @Override
    protected void syncToDate(LocalDate date)
    {
        //TODO
    }

    @Override
    protected void syncToTime(LocalTime time)
    {
        //TODO
    }

    @Override
    protected void placeSelected(Place place)
    {
        //TODO: there used to be a "return new AdapterView.OnItem... but its a void function.... why?
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                direction.set(position > 0 ? Ride.Direction.values()[position - 1] : null);

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14.0f));
                center = place.getLatLng();
                setMarker(center);

                if (radius != null)
                    setCircle(center, radius);

                //TODO data sync
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    @Override
    protected void tripTypeSelected(Ride.Direction direction) {}

    @Override
    protected void genderSelected(String gender)
    {
        ride.gender = gender;
    }

    public void onDriverNameChanged(CharSequence s, int start, int before, int count)
    {
        ride.driverName = s.toString();
    }

    public void onDriverNumberChanged(CharSequence s, int start, int before, int count)
    {
        ride.driverNumber = s.toString();
    }

    public AdapterView.OnItemSelectedListener onCarCapacitySelected()
    {
       return new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0)
                {
                    ride.carCapacity = Integer.valueOf(((TextView) view).getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
    }

    public void onRadiusChanged(CharSequence s, int start, int before, int count)
    {
        if(!s.toString().isEmpty())
        {
            try
            {
                this.radius = Double.valueOf(s.toString());
                setCircle(center, this.radius);
                //TODO data sync
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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(CALPOLY_LAT, CALPOLY_LNG), 14.0f));
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

            int padding = MetricsUtil.dpToPx(CruApplication.getContext(), 8);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu);
        }
    }

    private void populateBinds() {
        //set gender
        //set car capacity
        //set trip type
        //set times
        //TODO: get 2 sets of dates and times
        //set location

        //set map
        //TODO: somehow get a LatLng from an address
        //TODO: save a radius
        //LatLng latlng = new LatLng(ride.location.)
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14.0f));
        //setMarker(latlng);
        //setCircle(latlng, radius);
    }

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    public String getTime() {
        return ride.time.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
    }

    public String getDate() {
        return ride.time.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
    }
}
