package com.siemens.nextwork.tag.model.dto;

import com.siemens.nextwork.tag.enums.HCAssignedBy;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HCBridgeDemand {
    @Id
    private String hcAssignmentId;
    private HCAssignedBy assignedBy;
    private List<DataEntity> data;
}
