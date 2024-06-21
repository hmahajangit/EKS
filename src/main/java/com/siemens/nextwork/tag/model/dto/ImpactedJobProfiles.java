package com.siemens.nextwork.tag.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImpactedJobProfiles {
	
	private String trendAndBOId;
	private String migratedTrendAndBOId;
	private String jobProfileId;
	private String migratedJobProfileId;
	private String jobProfileName;
	private String impactOnHCDemand;
	private String impactOnSkill;

}
