package com.siemens.nextwork.tag.dto.tags;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagsRequestDTO {

    private String name;

    private String description;

    private String category;

    private String wsId;

    private String origin;

    private List<String> tagEntities;

}