package com.siemens.nextwork.tag.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@With
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Document(collection = "Tags_Data")
public class Tags {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String description;
    private String category;
    private String wsId;
    private String origin;
    private List<String> tagEntities;
    private Date createdOn;
    private Date updatedOn;
    private String createdBy;
    private String updatedBy;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
}
