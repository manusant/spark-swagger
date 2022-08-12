package io.github.manusant.ss.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact {
    private String name;
    private String url;
    private String email;

    public Contact name(String name) {
        this.setName(name);
        return this;
    }

    public Contact url(String url) {
        this.setUrl(url);
        return this;
    }

    public Contact email(String email) {
        this.setEmail(email);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Contact other = (Contact) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String url;
        private String email;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Contact build() {
            Contact contact = new Contact();
            contact.setName(name);
            contact.setUrl(url);
            contact.setEmail(email);
            return contact;
        }
    }
}