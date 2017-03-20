package com.crucentralcoast.app.data.converters;

import com.crucentralcoast.app.data.models.RideCheckResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class RideStatusConverter implements JsonDeserializer<RideCheckResponse.RideStatus>, JsonSerializer<RideCheckResponse.RideStatus>
{
    @Override
    public JsonElement serialize(RideCheckResponse.RideStatus src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.ordinal());
    }

    @Override
    public RideCheckResponse.RideStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        RideCheckResponse.RideStatus toReturn;
        switch (json.getAsInt())
        {
            case 0:
                toReturn = RideCheckResponse.RideStatus.NEITHER;
                break;
            case 1:
                toReturn = RideCheckResponse.RideStatus.DRIVER;
                break;
            case 2:
                toReturn = RideCheckResponse.RideStatus.PASSENGER;
                break;
            default:
                throw new JsonParseException("RideStatus Serialize error: Unexpected value: " + json.getAsString());
        }
        return toReturn;
    }
}
