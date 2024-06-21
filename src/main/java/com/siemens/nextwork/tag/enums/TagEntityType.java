package com.siemens.nextwork.tag.enums;

public enum TagEntityType {
    WORKSTREAM("Workstream"),
    JOBPROFILE("JobProfile"),
    SKILLS("Skills"),
    IMAPACT("Impact"),
    MEASURES("Measures"),
    JOBCLUSTER("JobCluster");

    public final String value;

    private TagEntityType(String value) {
        this.value = value;
    }

}