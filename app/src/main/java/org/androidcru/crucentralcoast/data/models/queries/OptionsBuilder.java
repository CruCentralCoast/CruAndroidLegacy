package org.androidcru.crucentralcoast.data.models.queries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class OptionsBuilder
{
    private JsonArray operands;

    public OptionsBuilder()
    {
        operands = new JsonArray();
    }

    public JsonObject build()
    {
        return JsonUtil.flatten(operands);
    }

    public OptionsBuilder addOption(JsonObject value)
    {
        operands.add(value);
        return this;
    }

    public OptionsBuilder addOption(OPTIONS option, JsonElement value)
    {
        JsonObject object = new JsonObject();
        object.add(option.serializedName, value);
        return addOption(object);
    }

    public OptionsBuilder addOption(OPTIONS option, Boolean value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(option.serializedName, value);
        return addOption(object);
    }

    public OptionsBuilder addOption(OPTIONS option, Double value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(option.serializedName, value);
        return addOption(object);
    }

    public OptionsBuilder addOption(OPTIONS option, Integer value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(option.serializedName, value);
        return addOption(object);
    }

    public OptionsBuilder addOption(OPTIONS option, Character value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(option.serializedName, value);
        return addOption(object);
    }

    public OptionsBuilder addOption(OPTIONS option, String value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(option.serializedName, value);
        return addOption(object);
    }

    public enum OPTIONS
    {
        SORT("sort"), LIMIT("limit");

        private String serializedName;

        OPTIONS(String serializedName)
        {
            this.serializedName = serializedName;
        }

        public String toString()
        {
            return serializedName;
        }
    }

}
