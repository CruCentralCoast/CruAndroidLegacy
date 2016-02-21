package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.app.FragmentManager;
import android.databinding.ObservableField;
import android.location.Address;
import android.view.View;
import android.widget.AdapterView;
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
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.util.MathUtil;
import org.androidcru.crucentralcoast.util.DisplayMetricsUtil;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class RideVM extends BaseRideVM
{
    public Ride ride;
    public Double radius;

    private GoogleMap map;
    private Marker marker;
    private Circle circle;
    private LatLng center;
    private LocalDate date;
    private LocalTime time;

    public boolean editing; //ride is being editted
    private static final double CALPOLY_LAT = 35.30021;
    private static final double CALPOLY_LNG = -120.66310;

    private ArrayList<Ride.Direction> directions;


    public RideVM(FragmentManager fm, Ride ride)
    {
        super(fm);
        this.ride = ride;
        direction = new ObservableField<>(null);
        editing = false;
        generateDirections();
    }

    public RideVM(FragmentManager fm, Ride ride, boolean editing)
    {
        super(fm);
        this.ride = ride;
        direction.set(ride.direction);
        radius = ride.radius;
        this.editing = editing;

        generateDirections();
    }

    private void generateDirections()
    {
        if(editing)
        {
            directions = new ArrayList<>();
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
        }
        else
        {
            directions = new ArrayList<>(Arrays.asList(Ride.Direction.TO, Ride.Direction.FROM, Ride.Direction.ROUNDTRIP));
        }
    }

    @Override
    protected void syncFromDate(LocalDate date)
    {
        //TODO
        // server only supports one datetime at the moment.
    }

    @Override
    protected void syncFromTime(LocalTime time)
    {
        //TODO
        // server only supports one datetime at the moment.
    }

    @Override
    protected void syncToDate(LocalDate date)
    {
        this.date = date;
        updateToDateTime();
    }

    @Override
    protected void syncToTime(LocalTime time)
    {
        this.time = time;
        updateToDateTime();

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

    @Override
    protected void tripTypeSelected(Ride.Direction direction)
    {
        ride.direction = direction;
    }

    @Override
    protected void genderSelected(String gender)
    {
        ride.gender = gender;
    }

    public int getGenderSelection()
    {
        if(ride.gender != null)
        {
            Ride.Gender[] genders = Ride.Gender.values();
            for (int i = 0; i < Ride.Gender.values().length; i++)
            {
                if (ride.gender.equals(genders[i].getValue()))
                {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    public int getCarCapacitySelection()
    {
        return editing ? ride.carCapacity + 1 : 0;
    }

    public int getTripSelection()
    {
        if(editing)
        {
            for (int i = 0; i < directions.size(); i++)
            {
                if(ride.direction == directions.get(i))
                {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    public String[] getTripOptions()
    {
        String[] options = new String[directions.size() + 1];
        options[0] = "Select Trip Type";
        for(int i = 1; i < directions.size() + 1; i++)
        {
            options[i] = directions.get(i - 1).getValueDetailed();
        }
        return options;
    }

    public void onDriverNameChanged(CharSequence s, int start, int before, int count)
    {
        ride.driverName = s.toString();
    }

    public void onDriverNumberChanged(CharSequence s, int start, int before, int count)
    {
        ride.driverNumber = s.toString();
    }

    public AdapterView.OnItemSelectedListener onGenderSelected()
    {
        return new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0)
                {
                    ride.gender = ((TextView) view).getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
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

            int padding = DisplayMetricsUtil.dpToPx(CruApplication.getContext(), 8);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu);
        }
    }

    public void setMap(Address addr) {
        Logger.d("trying to set to lat: " + addr.getLatitude() + " lng: " + addr.getLongitude());
        LatLng latlng = new LatLng(addr.getLatitude(), addr.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14.0f));
        setMarker(latlng);
        setCircle(latlng, ride.radius);
    }

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    public String getTime() {
        return ride.time.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
    }

    public String getDate() {
        return ride.time.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
    }

    private void updateToDateTime()
    {
        if(date != null && time != null)
        {
            ride.time = ZonedDateTime.of(date, time, ZoneId.systemDefault());
        }
    }
}
