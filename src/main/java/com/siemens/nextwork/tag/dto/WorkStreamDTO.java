package com.siemens.nextwork.tag.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkStreamDTO {
	
	private String uid;
	private String name;
	private String description;
	private String projectStage;
	private String scoping;
	private String selfRole;
	private Date createdOn;
	private String publishedStatus;
	private List<String> orgCodes;
	private List<String> gidList;
	
}
