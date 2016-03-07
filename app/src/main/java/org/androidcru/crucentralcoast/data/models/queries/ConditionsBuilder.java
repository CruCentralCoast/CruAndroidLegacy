package org.androidcru.crucentralcoast.data.models.queries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class ConditionsBuilder
{
    private OPERATOR operator;
    private JsonArray operands;
    private String field;

    public ConditionsBuilder()
    {
        operands = new JsonArray();
    }

    public JsonObject build()
    {
        JsonObject condition = new JsonObject();
        if(operator != null)
            condition.add(operator.serializedName, operands);
        else
        {
            condition.add(field, JsonUtil.flatten(operands));
        }
        return condition;
    }

    public ConditionsBuilder setCombineOperator(OPERATOR operator)
    {
        this.operator = operator;
        return this;
    }

    public ConditionsBuilder setField(String field)
    {
        this.field = field;
        return this;
    }


    public ConditionsBuilder addRestriction(JsonObject value)
    {
        operands.add(value);
        return this;
    }

    public ConditionsBuilder addRestriction(OPERATOR operator, JsonElement value)
    {
        JsonObject object = new JsonObject();
        object.add(operator.serializedName, value);
        return addRestriction(object);
    }

    public ConditionsBuilder addRestriction(OPERATOR operator, Boolean value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(operator.serializedName, value);
        return addRestriction(object);
    }

    public ConditionsBuilder addRestriction(OPERATOR operator, Double value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(operator.serializedName, value);
        return addRestriction(object);
    }

    public ConditionsBuilder addRestriction(OPERATOR operator, Integer value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(operator.serializedName, value);
        return addRestriction(object);
    }

    public ConditionsBuilder addRestriction(OPERATOR operator, Character value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(operator.serializedName, value);
        return addRestriction(object);
    }

    public ConditionsBuilder addRestriction(OPERATOR operator, String value)
    {
        JsonObject object = new JsonObject();
        object.addProperty(operator.serializedName, value);
        return addRestriction(object);
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
