package com.siemens.nextwork.tag.model.dto;

import jakarta.persistence.Id;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Trainings {

	@Id
	private String trainingId;
	private String name;
	private Double cost;
	private Integer minutes;
	private Integer hours;
	private String skillId;
	private String skillName;
	private String devPathId;
	protected String createdBy;
	protected Date createdOn;
	private String updatedBy;
	protected Date updatedOn;
	
}
