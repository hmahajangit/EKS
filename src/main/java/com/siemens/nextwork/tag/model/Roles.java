package com.siemens.nextwork.tag.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siemens.nextwork.tag.dto.WorkStreamDTO;
import com.siemens.nextwork.tag.model.dto.Member;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@With
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Document(collection = "Roles_Data")
public class Roles {

	@Id
	private String id;
	private String roleDisplayName;
	private String roleDescription;
	private String roleType;
	private List<String> gidList;
	private List<WorkStreamDTO> workstreamList;
	private List<String> directGidList;
	private List<Member> memberList;
	private String createdBy;
	private String createdByEmail;
	private String linkedGIDupdatedBy;
	private String linkedGIDupdatedByEmail;
	private Date date;
	private Date time;
	private String username;
	private String email;
	private boolean haveGIDList;
	private Boolean isDeleted = false;
	private Map<String, Integer> gidMap;
	

}
