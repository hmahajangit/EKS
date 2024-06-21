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
public class SkillAssignment {
	
	private String jobProfileId;
	private String migratedJobProfileId;
	private String skillAssignmentId;
	private String migratedSkillId;
	private String skillId;
	private String skillName;
	private String currentSkillLevel;
	private String futureSkillLevel;
	private String skillCategory;

	protected String createdBy;
	protected Date createdOn;
	private String updatedBy;
	protected Date updatedOn;
	
	private Boolean deleteFlag;


}
