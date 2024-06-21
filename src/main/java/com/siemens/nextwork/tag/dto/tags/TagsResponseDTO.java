package com.siemens.nextwork.tag.dto.tags;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagsResponseDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private String wsId;
    private String placeOfCreation;
    private List<String> tagEntities;
    private Date createdOn;
    private Date updatedOn;
    private String createdBy;
    private String updatedBy;
}
