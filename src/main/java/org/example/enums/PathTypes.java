package org.example.enums;

public enum PathTypes {
    SERVERS("servers"),
    GROUPS("groups"),
    LISTENER("listeners");

    private final String value;

    PathTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

