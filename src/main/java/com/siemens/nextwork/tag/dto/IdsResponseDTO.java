package com.siemens.nextwork.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "IdsResponseDTO", description = "Data object for Ids Response", oneOf = IdsResponseDTO.class)
public class IdsResponseDTO {

	private List<String> uids;
	
}
