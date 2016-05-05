package org.androidcru.crucentralcoast.data.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.threeten.bp.ZonedDateTime;

import java.lang.reflect.Type;

public class SnippetConverter implements JsonDeserializer<Snippet>
{
    @Override
    public Snippet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonElement content = json.getAsJsonObject().get("snippet");
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeConverter())
            .create();
        return gson.fromJson(content, typeOfT);
    }
}
