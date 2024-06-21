package com.siemens.nextwork.tag.repo;

import com.siemens.nextwork.tag.dto.tags.TagsGetResponseDTO;
import com.siemens.nextwork.tag.model.Tags;

import java.util.List;


public interface TagRepositoryExt {

    public List<Tags> findByTagsIds(List<String> ids);
    public List<Tags> findAllLocalTagsByWsId(String wsId);
    public List<TagsGetResponseDTO.SearchData> findLocalTagsDataByWsId(String wsId);
    public List<TagsGetResponseDTO.SearchData> findGlobalTagsData();
    public List<Tags> findAllGlobalTags();
}
