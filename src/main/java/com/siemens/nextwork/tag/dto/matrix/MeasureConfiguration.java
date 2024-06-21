package com.siemens.nextwork.tag.dto.matrix;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasureConfiguration extends MeasureCommonFields{

	private String tier1;
	private List<Tier2Data> tier2Data;

}
