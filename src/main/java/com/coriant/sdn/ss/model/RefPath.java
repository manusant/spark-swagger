package com.coriant.sdn.ss.model;

import com.coriant.sdn.ss.model.parameters.Parameter;
import com.coriant.sdn.ss.model.refs.GenericRef;
import com.coriant.sdn.ss.model.refs.RefFormat;
import com.coriant.sdn.ss.model.refs.RefType;
import com.coriant.sdn.ss.model.properties.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Helmsdown on 7/8/15.
 *
 * This class extends directly from Path for now. At some future date we will need
 * to make {@link Path} an interface to follow the pattern established by
 * {@link Model}, {@link Property} and {@link Parameter}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefPath extends Path {

    private GenericRef genericRef;

    public RefPath() {
    }

    public RefPath(String ref) {
        set$ref(ref);
    }

    public void set$ref(String ref) {
        this.genericRef = new GenericRef(RefType.PATH, ref);
    }

    public String get$ref() {
        return genericRef.getRef();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefPath refPath = (RefPath) o;

        return !(genericRef != null ? !genericRef.equals(refPath.genericRef) : refPath.genericRef != null);

    }

    @Override
    public int hashCode() {
        return genericRef != null ? genericRef.hashCode() : 0;
    }

    @JsonIgnore
    public RefFormat getRefFormat() {
        return this.genericRef.getFormat();
    }

}
