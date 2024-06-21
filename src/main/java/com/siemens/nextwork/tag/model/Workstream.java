package com.siemens.nextwork.tag.model;

import com.siemens.nextwork.tag.enums.PublishType;
import com.siemens.nextwork.tag.enums.StageType;
import com.siemens.nextwork.tag.model.dto.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "workstream")
public class Workstream {

	@Id
	private String uid;
	private String migratedOldUid;
	private String name;
	
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	
	private Date implementedOn;
	
	private String scopingVersion;
	private StageType stage;
	private String projectStatus;
	private PublishType publishedStatus;
	private Boolean localAdminScopedWorkstream = false;
	
	private Boolean isDeleted = false;
	private Date createdOn;
	private Date publishedOn; 
	private String createdBy;
	
	private String selfRole;
	private int memberCount;
	private List<Users> users;
	private AssigneeDetails assigneeDetails;
	
	private String scoping;
	private int projectHC;
	private List<GidData> gidList;
	private List<String> gidSummary;
	private Map<String,Integer> orgCodes;

	private Boolean isMigratedWS = false;
	private List<WsMigrationData> wsMigrationData;
	private Boolean isCopiedWS = false;
	private Summary summary;
	
	private List<JobProfile> jobProfiles;
	private Map<String,List<String>> statusQuoGripDetails;
	private List<JobCluster> jobClusters;
	private List<Skills> skills;
	private List<TrendsAndBizOutlook> trends;
	private List<NeedForAction> needForAction;
	private List<Matrix> matrixes;
	private List<DevelopmentPath> developmentPath;
	private List<LatestMatrixModel> latestMatrixDetails;
	private List<Levers> levers;
	private HCBridgeSupply hcAssignmentEntitySupply;
	private HCBridgeDemand hcAssignmentEntityDemand;
	private List<String> tags;

	private String comment;

	private Boolean hasLever = false;


	@Override
	public String toString() {
		return "Workstream [uid=" + uid + ", name=" + name + ", statusQuoGripDetails=" + statusQuoGripDetails + "]";
	}


	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Workstream other = (Workstream) obj;
		return Objects.equals(uid, other.uid);
	}
	
	

}
