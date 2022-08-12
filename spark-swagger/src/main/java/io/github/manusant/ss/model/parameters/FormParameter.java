package io.github.manusant.ss.model.parameters;

public class FormParameter extends AbstractSerializableParameter<FormParameter> {

    public FormParameter() {
        super.setIn("formData");
    }

    @Override
    protected String getDefaultCollectionFormat() {
        return "multi";
    }
}
