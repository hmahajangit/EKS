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
public class Summary {
	private String uid;
	private String name;
	private String description;
	private List<String> orgCode;
	private String employeeStructure;
	private String createdBy;
	private Date createdOn;
	private int startYear;
	private int startMonth;

	private int endYear;
	private List<LocationCountry> locationCountry;
	private List<Users> users;
	private Boolean isTestProject;



}
