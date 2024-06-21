package com.siemens.nextwork.tag.service;

import com.siemens.nextwork.tag.dto.IdsResponseDTO;
import com.siemens.nextwork.tag.dto.tags.DeleteTagsDTO;
import com.siemens.nextwork.tag.dto.tags.TagsGetResponseDTO;
import com.siemens.nextwork.tag.dto.tags.TagsRequestDTO;
import com.siemens.nextwork.tag.dto.tags.TagsResponseDTO;


public interface TagService {

    TagsResponseDTO createNewTag(String userEmail, TagsRequestDTO tagRequestDTO);
    TagsResponseDTO updateTag(String userEmail, TagsRequestDTO tagRequestDTO,String tagId);
    IdsResponseDTO deleteTags(String userEmail, String workStreamId, DeleteTagsDTO deleteTagsDTO, String action);
    TagsGetResponseDTO getTags(String userEmail, String workStreamId, String purpose);
}
