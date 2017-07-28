package com.crucentralcoast.app.data.converters;

import com.crucentralcoast.app.data.models.Ride;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GenderConverter implements JsonDeserializer<Ride.Gender>, JsonSerializer<Ride.Gender>
{

    @Override
    public JsonElement serialize(Ride.Gender src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }

    @Override
    public Ride.Gender deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        return Ride.Gender.getFromId(json.getAsInt());
    }
}