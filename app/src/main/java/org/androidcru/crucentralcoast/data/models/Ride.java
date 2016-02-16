package org.androidcru.crucentralcoast.data.models;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

public class Ride
{
    @SerializedName("driverName") public String driverName;
    @SerializedName("driverNumber") public String driverNumber;
    @SerializedName("gender") public String gender;
    @SerializedName("event") public String eventId;
    @SerializedName("time") public ZonedDateTime time;
    @SerializedName("location") public Location location;
    @SerializedName("passengers") public ArrayList<String> passengers;
    @SerializedName("radius") public double radius;
    @SerializedName("direction") public Direction direction;
    public int carCapacity;

    public Ride(String driverName, String driverNumber, String gender, String eventId, ZonedDateTime time, Location location, ArrayList<String> passengers)
    {
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.gender = gender;
        this.eventId = eventId;
        this.time = time;
        this.location = location;
        this.passengers = passengers;
    }

    public Ride() {}

    public enum Direction
    {
        TO("to"),
        FROM("from"),
        ROUNDTRIP("both");

        private String direction;

        Direction(String direction)
        {
            this.direction = direction;
        }

        public String getValue()
        {
            return direction;
        }

        public String[] getAll()
        {
            Direction[] directions = Direction.values();
            String[] directionStr = new String[directions.length];
            for(int i = 0; i < directions.length; i++)
            {
                directionStr[i] = directions[i].getValue();
            }
            return directionStr;
        }
    }

    //can't decide if this should be used
    public enum Gender
    {
        ANY("Any"),
        MAN("Man"),
        WOMAN("Woman");

        private String gender;

        Gender(String gender)
        {
            this.gender = gender;
        }

        public String getValue()
        {
            return gender;
        }

        public String[] getAll()
        {
            Gender[] genders = Gender.values();
            String[] genderStr = new String[genders.length];
            for(int i = 0; i < genders.length; i++)
            {
               genderStr[i] = genders[i].getValue();
            }
            return genderStr;
        }
    }
}
