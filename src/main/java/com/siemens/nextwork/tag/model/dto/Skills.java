package com.siemens.nextwork.tag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skills {
	
	private String uid;
	private String lexId;
	private String migratedOldUid;
	private String name;
	private String description;
	private Boolean isOriginFuture;
	private SkillCategory skillCategory;
	private Boolean mySkill;
	private String customSkillName;
	private String ownSkillName;
	private List<String> tags;
	protected String createdBy;
	protected Date createdOn;
	private String updatedBy;
	protected Date updatedOn;
	private Boolean isDeleted;

}
