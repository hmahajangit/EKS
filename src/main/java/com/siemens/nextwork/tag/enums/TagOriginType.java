package com.siemens.nextwork.tag.enums;

public enum TagOriginType {
    TAG_MANAGEMENT("Tag Management"),
    DIRECT_ASSIGNEMENT("Direct Assignment");

    public final String value;

    private TagOriginType(String value) {
        this.value = value;
    }

}