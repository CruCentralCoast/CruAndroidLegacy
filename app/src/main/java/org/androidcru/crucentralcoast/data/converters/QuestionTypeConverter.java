package org.androidcru.crucentralcoast.data.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.androidcru.crucentralcoast.data.models.MinistryQuestion;

import java.lang.reflect.Type;

public class QuestionTypeConverter implements JsonDeserializer<MinistryQuestion.Type>, JsonSerializer<MinistryQuestion.Type>
{

    @Override
    public JsonElement serialize(MinistryQuestion.Type src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.value);
    }

    @Override
    public MinistryQuestion.Type deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        MinistryQuestion.Type toReturn;
        switch (json.getAsString())
        {
            case "select":
                toReturn = MinistryQuestion.Type.SELECT;
                break;
            case "text":
                toReturn = MinistryQuestion.Type.TEXT;
                break;
            default:
                throw new JsonParseException("MinistryQuestion.Type Serialize error: Unexpected value: " + json.getAsString());
        }
        return toReturn;
    }
}