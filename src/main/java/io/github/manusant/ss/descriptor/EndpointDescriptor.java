package io.github.manusant.ss.descriptor;

import io.github.manusant.ss.model.ExternalDocs;
import io.github.manusant.ss.model.Tag;

/**
 * @author manusant
 */
public class EndpointDescriptor {

    private String nameSpace;
    private String path;
    private Tag tag;
    private String description;
    private ExternalDocs externalDoc;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalDocs getExternalDoc() {
        return externalDoc;
    }

    public void setExternalDoc(ExternalDocs externalDoc) {
        this.externalDoc = externalDoc;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public static Builder endpointPath(final String path) {
        return new Builder().withPath(path);
    }

    public static final class Builder {
        private String path;
        private String description;
        private ExternalDocs externalDoc;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withExternalDoc(ExternalDocs externalDoc) {
            this.externalDoc = externalDoc;
            return this;
        }

        private String getTag() {
            return path.contains("/") ? path.substring(1) : path;
        }

        public EndpointDescriptor build() {
            EndpointDescriptor endpointDescriptor = new EndpointDescriptor();
            endpointDescriptor.setPath(path);
            endpointDescriptor.setTag(Tag.newBuilder()
                    .withName(getTag())
                    .withDescription(description)
                    .withExternalDocs(externalDoc)
                    .build());
            endpointDescriptor.setDescription(description);
            endpointDescriptor.setExternalDoc(externalDoc);
            return endpointDescriptor;
        }
    }
}
