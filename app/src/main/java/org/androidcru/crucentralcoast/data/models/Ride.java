package org.androidcru.crucentralcoast.data.models;

import android.location.Address;

import com.google.gson.annotations.SerializedName;

import org.androidcru.crucentralcoast.CruApplication;
import org.parceler.Parcel;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Ride
{
    public static final String serializedDriverName = "driverName";
    public static final String serializedDriverNumber = "driverNumber";
    public static final String serializedGender = "gender";
    public static final String serializedEvent = "event";
    public static final String serializedTime = "time";
    public static final String serializedLocation = "location";
    public static final String serializedPassengers = "passengers";
    public static final String serializedRadius = "radius";
    public static final String serializedDirection = "direction";
    public static final String serializedGcmId = "gcm_id";
    public static final String serializedId = "_id";
    public static final String serializedSeats = "seats";

    @SerializedName(serializedDriverName) public String driverName;
    @SerializedName(serializedDriverNumber) public String driverNumber;
    @SerializedName(serializedGender) public String gender;
    @SerializedName(serializedEvent) public String eventId;
    @SerializedName(serializedTime) public ZonedDateTime time;
    @SerializedName(serializedLocation) public Location location;
    @SerializedName(serializedPassengers) public ArrayList<String> passengerIds;
    @SerializedName(serializedRadius) public double radius;
    @SerializedName(serializedDirection) public Direction direction;
    @SerializedName(serializedGcmId) public String gcmID;
    @SerializedName(serializedId) public String id;
    @SerializedName(serializedSeats) public int carCapacity;

    public Address address;
    public List<Passenger> passengers;
    public CruEvent event;

    public Ride() {}

    // name, phone, gender, event id, car capacity, direction, time, location
    // need gcmID, radius
    public Ride(String driverName, String driverNumber, String gender, String eventId,
                ZonedDateTime time, Location location, //ArrayList<String> passengerIds,
                double radius, Direction direction, //String gcmID,
                int carCapacity)
    {
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.gender = gender;
        this.eventId = eventId;
        this.time = time;
        this.location = location;
        this.passengerIds = new ArrayList<>();
        this.radius = radius;
        this.direction = direction;
        this.gcmID = CruApplication.getGCMID();//gcmID;
        this.carCapacity = carCapacity;
    }

    public boolean isEmpty()
    {
        return id == null;
    }

    public enum Direction
    {
        TO("to", "To Event"),
        FROM("from", "From Event"),
        ROUNDTRIP("both", "Round-trip");

        private String direction;
        private String directionDetailed;

        Direction(String direction, String directionDetailed)
        {
            this.direction = direction;
            this.directionDetailed = directionDetailed;
        }

        public String getValue()
        {
            return direction;
        }

        public String getValueDetailed() {
            return directionDetailed;
        }

        public static String[] getAll()
        {
            Direction[] directions = Direction.values();
            String[] directionStr = new String[directions.length];
            for(int i = 0; i < directions.length; i++)
            {
                directionStr[i] = directions[i].getValueDetailed();
            }
            return directionStr;
        }
    }
}
