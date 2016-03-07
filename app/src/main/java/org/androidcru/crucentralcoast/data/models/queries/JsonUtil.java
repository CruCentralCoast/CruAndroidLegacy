package org.androidcru.crucentralcoast.data.models.queries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonUtil
{
    //merge JsonObjects in a JsonArray into a single JsonObject
    public static JsonObject flatten(JsonArray array)
    {
        JsonObject flattenedObject = new JsonObject();
        for(JsonElement e : array)
        {
            for (Map.Entry<String, JsonElement> entry : e.getAsJsonObject().entrySet())
            {
                flattenedObject.add(entry.getKey(), entry.getValue());
            }
        }
        return flattenedObject;
    }
}
