package io.github.manusant.ss.model.parameters;

public class QueryParameter extends AbstractSerializableParameter<QueryParameter> {

    public QueryParameter() {
        super.setIn("query");
    }

    @Override
    protected String getDefaultCollectionFormat() {
        return "multi";
    }
}
