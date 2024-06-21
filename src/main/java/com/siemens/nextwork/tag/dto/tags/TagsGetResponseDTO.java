package com.siemens.nextwork.tag.dto.tags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TagsGetResponseDTO {
    private List<TagsDTO> listData;
    private List<SearchData> searchData;
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class TagsDTO {
        private String name;
        private String description;
        private Boolean isActive;
        private Boolean isDeleted;
        private String category;
        private String origin;
        private List<String> tagEntities;
        private List<Association> associations;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Association {
        private String itemType;
        private String itemName;
        private String itemId;
        private Boolean isOriginFuture;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class SearchData {
        private String name;
        private String id;
        private String category;
        private List<String> tagEntities;
    }
}