package com.siemens.nextwork.tag.enums;

public enum LeverDirection {
    POSITIVE("Positive"),
    NEGATIVE("Negative"),
    STABLE("Stable");

    public final String value;

    private LeverDirection(String value) {
        this.value = value;
    }

}
