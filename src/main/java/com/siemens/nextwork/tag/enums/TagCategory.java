package com.siemens.nextwork.tag.enums;

public enum TagCategory {
    GLOBAL("Global"),
    LOCAL("Local");

    public final String value;

    private TagCategory(String value) {
        this.value = value;
    }

}