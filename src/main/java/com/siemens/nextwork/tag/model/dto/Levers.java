package com.siemens.nextwork.tag.model.dto;

import com.siemens.nextwork.tag.enums.LeverCategory;
import com.siemens.nextwork.tag.enums.LeverDirection;
import com.siemens.nextwork.tag.enums.SourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Levers {

    @Id
    private String uid;

    private String leverName;

    private String description;

    private Integer index;

    private LeverDirection leverDirection;

    private LeverCategory leverCategory;

    private Boolean isGlobal = false;

    private Boolean isAssigned = false;

    private SourceType source;

}
