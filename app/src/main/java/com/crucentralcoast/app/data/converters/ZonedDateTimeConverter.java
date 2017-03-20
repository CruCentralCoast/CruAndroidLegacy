package com.crucentralcoast.app.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class ZonedDateTimeConverter implements JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime>
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static String format(ZonedDateTime dateTime)
    {
        return FORMATTER.format(dateTime.withZoneSameLocal(ZoneOffset.UTC));
    }

    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {

        //TODO if the server can ever handle timezones properly, change to withZoneSameInstant
        return new JsonPrimitive(format(src));
    }

    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        //TODO if the server can ever handle timezones properly, change to withZoneSameInstant
        return  FORMATTER.parse(json.getAsString(), ZonedDateTime.FROM).withZoneSameLocal(ZoneOffset.UTC);
    }
}
