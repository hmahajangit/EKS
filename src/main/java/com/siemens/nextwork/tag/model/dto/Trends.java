package com.siemens.nextwork.tag.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Trends {
	
	private String uid;
	private String impactOnHCDemand;
	private String impactOnSkill;
	private String createdBy;
	private Date createdOn;
	private String updatedBy;
	private Date updatedOn;
	private String impactType;
	private String impactCategory;
	private String impactSubCategory;



}
