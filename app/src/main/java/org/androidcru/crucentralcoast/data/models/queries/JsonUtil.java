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
                if(!entry.getValue().isJsonNull())
                {
                    flattenedObject.add(entry.getKey(), entry.getValue());
                }
            }
        }
        return flattenedObject;
    }

    public static boolean isEmpty(JsonObject object)
    {
        return object.entrySet().isEmpty();
    }
}
