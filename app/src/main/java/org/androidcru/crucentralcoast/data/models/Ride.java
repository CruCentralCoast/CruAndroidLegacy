package org.androidcru.crucentralcoast.data.models;

import android.location.Address;

import com.google.gson.annotations.SerializedName;

import org.androidcru.crucentralcoast.CruApplication;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

public class Ride
{
    @SerializedName("driverName") public String driverName;
    @SerializedName("driverNumber") public String driverNumber;
    @SerializedName("gender") public String gender;
    @SerializedName("event") public String eventId;
    @SerializedName("time") public ZonedDateTime time;
    @SerializedName("location") public Location location;
    @SerializedName("passengers") public ArrayList<String> passengerIds;
    @SerializedName("radius") public double radius;
    @SerializedName("direction") public Direction direction;
    @SerializedName("gcm_id") public String gcmID;
    @SerializedName("_id") public String id;
    public int carCapacity;
    public Address address;
    public transient List<Passenger> passengers;

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
        return id.isEmpty();
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
