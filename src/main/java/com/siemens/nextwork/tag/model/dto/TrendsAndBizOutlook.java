package com.siemens.nextwork.tag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TrendsAndBizOutlook {
	
	@Id
	private String uid;	
	private String impactType;
	private String impactCategory;
	private String impactSubCategory;
	private String description;
	private Boolean isDeleted =false;
	private List<ImpactedJobProfiles> impactedJobProfiles;
	private List<String> tags;

}
