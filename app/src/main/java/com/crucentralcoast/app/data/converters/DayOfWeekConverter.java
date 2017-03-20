package com.crucentralcoast.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.format.TextStyle;

import java.lang.reflect.Type;
import java.util.Locale;

public class DayOfWeekConverter implements JsonDeserializer<DayOfWeek>, JsonSerializer<DayOfWeek>
{

    @Override
    public DayOfWeek deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        return DayOfWeek.valueOf(json.getAsString().toUpperCase());
    }

    @Override
    public JsonElement serialize(DayOfWeek src, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(src.getDisplayName(TextStyle.FULL, Locale.getDefault()));
    }
}
