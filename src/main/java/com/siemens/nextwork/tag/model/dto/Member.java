package com.siemens.nextwork.tag.model.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
	private String uid;
	private String memberName;
	private String memberEmail;

}
