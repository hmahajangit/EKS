package com.siemens.nextwork.tag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siemens.nextwork.tag.enums.GripTaskType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document
public class GripTaskDTO {
	
	@Id
	private String gripTaskId;
	private String gripTaskDetail;
	private GripTaskType gripTaskType;


}
