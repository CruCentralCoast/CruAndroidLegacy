package org.androidcru.crucentralcoast.data.models.queries;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Query
{
    @SerializedName("conditions") public JsonObject conditions;
    @SerializedName("projection") public StringBuilder projections;
    @SerializedName("options") public JsonObject options;

    public static class Builder
    {
        private Query query;

        public Builder()
        {
            query = new Query();
        }

        public Query build()
        {
            if(query.projections != null)
                query.projections.setLength(query.projections.length() - 1);
            return query;
        }

        public Builder setCondition(JsonObject conditions)
        {
            query.conditions = conditions;
            return this;
        }

        public Builder addProjection(String projection)
        {
            query.projections.append(projection).append(" ");
            return this;
        }

        public Builder setOptions(JsonObject options)
        {
            query.options = options;
            return this;
        }

    }
}
