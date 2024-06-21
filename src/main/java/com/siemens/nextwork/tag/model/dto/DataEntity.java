package com.siemens.nextwork.tag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataEntity {
    private Integer aggregatedHC;
    private Integer futureHC;
    private Double futureHCPercentage;
    private Integer differenceHC;
    private Double differenceHCPercentage;
    private String leverId;
    private String leverName;
    private Integer leverIndex;
    private String leverType;
    private String jobProfileId;
    private String jobProfileName;
    private List<YearDataEntity> yearData;
}
