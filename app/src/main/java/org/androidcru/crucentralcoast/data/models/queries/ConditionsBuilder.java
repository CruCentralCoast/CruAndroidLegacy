package org.androidcru.crucentralcoast.data.models.queries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class ConditionsBuilder
{
    private OPERATOR operator;
    private JsonArray operands;

    public ConditionsBuilder()
    {
        operands = new JsonArray();
    }

    public JsonObject build()
    {
        JsonObject condition = new JsonObject();
        condition.add(operator.serializedName, operands);
        return condition;
    }

    public ConditionsBuilder setOperator(OPERATOR operator)
    {
        this.operator = operator;
        return this;
    }

    public ConditionsBuilder addOperand(JsonObject value)
    {
        operands.add(value);
        return this;
    }

    public ConditionsBuilder addOperand(String key, JsonElement value)
    {
        JsonObject object = new JsonObject();
        object.add(key, value);
        return addOperand(object);
    }

    public ConditionsBuilder addOperand(String key, Boolean value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        return addOperand(object);
    }

    public ConditionsBuilder addOperand(String key, Double value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        return addOperand(object);
    }

    public ConditionsBuilder addOperand(String key, Integer value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        return addOperand(object);
    }

    public ConditionsBuilder addOperand(String key, Character value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        return addOperand(object);
    }

    public ConditionsBuilder addOperand(String key, String value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(key, value);
        return addOperand(object);
    }


    public enum OPERATOR
    {
        EQUALS("$equals"), AND("$and"), OR("$or"), GTE("$gte"), GT("$gt"),
        LTE("$lte"), LT("$lt"), IN("$in"), REGEX("$regex"), EXISTS("$exists"),
        NEAR("$near"), MAX_DISTANCE("$max_distance");

        private String serializedName;

        OPERATOR(String serializedName)
        {
            this.serializedName = serializedName;
        }

        public String toString()
        {
            return serializedName;
        }
    }
}
