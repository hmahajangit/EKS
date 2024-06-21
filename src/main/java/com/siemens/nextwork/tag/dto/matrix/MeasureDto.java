package com.siemens.nextwork.tag.dto.matrix;

import com.siemens.nextwork.tag.enums.MatrixMeasureType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MeasureDto {

	private String uid;
	private String statusQuoJPId;
	private String futureStateJPId;
	private MatrixMeasureType measuresType;
	private List<MeasureConfiguration> measureConfiguration;
	private List<String> tags;
	private LocalDate createdOn;
    private String createdBy;
    private LocalDate updatedOn;
    private String updatedBy;

}
