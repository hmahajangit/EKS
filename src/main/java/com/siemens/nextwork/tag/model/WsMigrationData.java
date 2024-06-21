package com.siemens.nextwork.tag.model;

import com.siemens.nextwork.tag.model.dto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WsMigrationData {

	private String uid;
	private Summary summary;
	private List<JobProfile> jobProfiles;
	private List<JobCluster> jobClusters;
	private List<Skills> skills;
	private List<TrendsAndBizOutlook> trendsAndBizOutlook;
	private List<NeedForAction> needForAction;
	private List<Matrix> matrixes;
	private List<DevelopmentPath> developmentPath;
}
