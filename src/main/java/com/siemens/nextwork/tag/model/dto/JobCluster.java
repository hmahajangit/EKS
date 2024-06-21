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
public class JobCluster {
	
	private String uid;
	private String name;
	private Boolean isOriginFuture;
	private List<JobProfile> jobProfile;

}
