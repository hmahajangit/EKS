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
public class DevelopmentPath {
	
	private String devPathId;
	private String matrixId;
	
	private String linkId;
	
	private String migratedMatrixId;

	private String statusQuoJobProfileId;
	
	private String migratedStatusQuoJobProfileId;

	private String statusQuoJobProfileName;

	private String futureStateJobProfileId;
	
	private String migratedFutureStateJobProfileId;

	private String futureStateJobProfileName;
	
	private Boolean futureStateJobProfileIsTwin;

	private String measure;

	private String status;

	private Integer assignedHC;
	
	private Integer headcountEffected;

	private List<Trainings> trainings;

}
