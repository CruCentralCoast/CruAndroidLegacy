package com.crucentralcoast.app.data.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Parcel
public final class Ride
{
    public static final String sDriverName = "driverName";
    public static final String sDriverNumber = "driverNumber";
    public static final String sGender = "gender";
    public static final String sEvent = "event";
    public static final String sTime = "time";
    public static final String sLocation = "location";
    public static final String sPassengers = "passengers";
    public static final String sRadius = "radius";
    public static final String sDirection = "direction";
    public static final String sGcmId = "gcm_id";
    public static final String sId = "_id";
    public static final String sSeats = "seats";

    @SerializedName(sDriverName) public String driverName;
    @SerializedName(sDriverNumber) public String driverNumber;
    @SerializedName(sGender) public Gender gender;
    @SerializedName(sEvent) public String eventId;
    @SerializedName(sTime) public ZonedDateTime time;
    @SerializedName(sLocation) public Location location;
    @SerializedName(sPassengers) public ArrayList<String> passengerIds;
    @SerializedName(sRadius) public double radius;
    @SerializedName(sDirection) public Direction direction;
    @SerializedName(sGcmId) public String gcmID;
    @SerializedName(sId) public String id;
    @SerializedName(sSeats) public int carCapacity;

    public List<Passenger> passengers;
    public CruEvent event;
    public double distance;

    @ParcelConstructor
    public Ride(String driverName, String driverNumber, Gender gender, String eventId,
                ZonedDateTime time, Location location, double radius, Direction direction,
                String gcmID, int carCapacity)
    {
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.gender = gender;
        this.eventId = eventId;
        this.time = time;
        this.location = location;
        this.radius = radius;
        this.direction = direction;
        this.gcmID = gcmID;
        this.carCapacity = carCapacity;
    }

    public boolean isEmpty()
    {
        return id == null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ride ride = (Ride) o;

        return id.equals(ride.id);

    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
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

    public enum Gender
    {
        NOT_KNOWN(0, "Not Known"), MALE(1, "Male"), FEMALE(2, "Female"), NOT_APPLICABLE(9, "Not Applicable");

        private int id;
        private String colloquial;

        Gender(int id, String colloquial)
        {
            this.id = id;
            this.colloquial = colloquial;
        }

        public static int getAmount()
        {
            return 4;
        }

        public int getId()
        {
            return id;
        }

        public String getColloquial()
        {
            return colloquial;
        }

        public static List<String> getColloquials()
        {
            return Arrays.asList(NOT_KNOWN.colloquial, MALE.colloquial, FEMALE.colloquial, NOT_APPLICABLE.colloquial);
        }

        public static Gender getFromId(int id)
        {
            Gender toReturn;
            switch (id)
            {
                case 0:
                    toReturn = NOT_KNOWN;
                    break;
                case 1:
                    toReturn = MALE;
                    break;
                case 2:
                    toReturn = FEMALE;
                    break;
                default:
                    toReturn = NOT_APPLICABLE;
            }
            return toReturn;
        }

        public static Gender getFromColloquial(String colloquial)
        {
            Gender toReturn = NOT_APPLICABLE;

            if(colloquial != null)
            {
                switch (colloquial)
                {
                    case "Not Known":
                        toReturn = Ride.Gender.NOT_KNOWN;
                        break;
                    case "Male":
                        toReturn = Ride.Gender.MALE;
                        break;
                    case "Female":
                        toReturn = Ride.Gender.FEMALE;
                        break;
                    default:
                        toReturn = Ride.Gender.NOT_APPLICABLE;
                }
            }
            return toReturn;
        }
    }
}
