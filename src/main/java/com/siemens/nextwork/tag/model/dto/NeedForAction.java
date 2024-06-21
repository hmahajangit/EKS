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
public class NeedForAction {
	
	private String jobProfileId;
	private String jobProfileName;
	private Integer futureHCDemand;
	private Integer futureHCSupply;
	private Integer currentHCSupply;
	private List<Skills> skills;
    private Boolean isOriginFuture;
    private Boolean isTwin;

}
