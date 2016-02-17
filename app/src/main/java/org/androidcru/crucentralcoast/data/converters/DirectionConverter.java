package org.androidcru.crucentralcoast.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.androidcru.crucentralcoast.data.models.Ride;

import java.lang.reflect.Type;

public class DirectionConverter implements JsonDeserializer<Ride.Direction>, JsonSerializer<Ride.Direction>
{

    @Override
    public JsonElement serialize(Ride.Direction src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getValue());
    }

    @Override
    public Ride.Direction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Ride.Direction toReturn;
        switch (json.getAsString())
        {
            case "to":
                toReturn = Ride.Direction.TO;
                break;
            case "from":
                toReturn = Ride.Direction.FROM;
                break;
            case "both":
                toReturn = Ride.Direction.ROUNDTRIP;
                break;
            default:
                throw new JsonParseException("Direction Serialize error: Unexpected value: " + json.getAsString());
        }
        return toReturn;
    }
}