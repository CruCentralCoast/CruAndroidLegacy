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
        {
            JsonArray nonEmptyArray = new JsonArray();
            for(JsonElement e : operands)
            {
                if(!JsonUtil.isEmpty(e.getAsJsonObject()))
                {
                    nonEmptyArray.add(e);
                }
            }
            condition.add(operator.serializedName, nonEmptyArray);
        }
        else
        {
            JsonObject flattened = JsonUtil.flatten(operands);
            if(!JsonUtil.isEmpty(flattened))
                condition.add(field, flattened);
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


    private ConditionsBuilder addRestriction(JsonObject value)
    {
        operands.add(value);
        return this;
    }

    public ConditionsBuilder addRestriction(ConditionsBuilder value)
    {
        operands.add(value.build());
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
