package io.github.manusant.ss.model.parameters;

public class HeaderParameter extends AbstractSerializableParameter<HeaderParameter> {

    public HeaderParameter() {
        super.setIn("header");
    }
}
