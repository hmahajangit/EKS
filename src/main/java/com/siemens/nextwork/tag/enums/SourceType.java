package com.siemens.nextwork.tag.enums;

public enum SourceType {
    LEVER("Lever"),
    IMPACT("Impact");

    public final String value;

    private SourceType(String value) {
        this.value = value;
    }

}
