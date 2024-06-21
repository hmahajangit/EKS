package com.siemens.nextwork.tag.model.dto;

import lombok.*;

import java.util.List;

@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IdResponseDTOList {
	private List<String> uids;

}
