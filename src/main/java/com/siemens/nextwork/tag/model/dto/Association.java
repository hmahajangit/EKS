package com.siemens.nextwork.tag.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Association {

    private String associationEntity;
    private String id;
    private String wsId;

}
