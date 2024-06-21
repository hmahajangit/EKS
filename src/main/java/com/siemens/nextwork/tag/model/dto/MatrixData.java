package com.siemens.nextwork.tag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatrixData {
	
	private String linkId;
	private String migratedLinkId;
	private String matrixId;
	private String statusQuoJobProfileId;
	private String migratedStatusQuoJobProfileId;
	private String futureStateJobProfileId;
	private String migratedFutureStateJobProfileId;
	private String measure;
	private Integer effectedHC;
	private String status;

	protected String createdBy;
	protected Date createdOn;
	private String updatedBy;
	protected Date updatedOn;
	
	@Override
	public String toString() {
		return "{\"linkId\":\"" + linkId + "\", \"statusQuoJobProfileId\":\"" + statusQuoJobProfileId
				+ "\", \"futureStateJobProfileId\":\"" + futureStateJobProfileId + "\", \"measure\":\"" + measure
				+ "\", \"effectedHC\":\"" + effectedHC + "\"}";
	}
	
}
