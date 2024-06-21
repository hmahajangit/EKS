package com.siemens.nextwork.tag.model.dto;


import com.siemens.nextwork.tag.dto.matrix.MeasureDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LatestMatrixModel {
	@Id
	private String uid;
	private String matrixName;
    private String description;
    private AssociatedJobProfiles associatedJobProfiles;
    private List<MeasureDto> measures;
}
