package com.siemens.nextwork.tag.model.dto;

import com.siemens.nextwork.tag.dto.GripTaskDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class JobProfile {
	
	@Id
	private String uid;
	private String migratedOldUid;
	private Boolean isDeleted = false;
	private String name;
	private String description;
	private String gripName;
	private List<String> gripPositionType;
	private List<String> gripPostion;
	private List<String> gripCodeCatalog;
	private List<GripTaskDTO> gripTask;
	private Integer currentHeadCountSupply;
	private Integer futureHeadCountSupply;
	private Integer futureHeadCountDemand;
	private Integer assignedFutureHCDemand;
	private Integer assignedFutureHCSupply;
	private Integer statusQuoHCAssigned=0;
	private Integer futureStateHCAssigned=0;
	private ValueStream valueStream;
	private String valueStreamDescription;
	private String cluster;
	private Boolean isOriginFuture;
	private Boolean isTwin;
	private List<String> jobFamily;
	private List<String> gidList;
	private String gidMappingStatus;
	private List<SkillAssignment> skillAssignments;
	@DBRef(lazy = true)
	private List<String> tags;
	private Boolean isMigrated = false;

}
