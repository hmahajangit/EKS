package com.siemens.nextwork.tag.enums;

public enum GripTaskType {
	DEFAULT("Default"),
	CUSTOM("Custom"),
	DEPRECATE("Deprecate");
	
	public final String value;

    private GripTaskType(String value) {
        this.value = value;
    }

}
