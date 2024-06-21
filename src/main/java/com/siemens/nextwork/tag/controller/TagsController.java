package com.siemens.nextwork.tag.controller;

import com.siemens.nextwork.tag.constants.NextworkConstants;
import com.siemens.nextwork.tag.dto.IdsResponseDTO;
import com.siemens.nextwork.tag.dto.tags.DeleteTagsDTO;
import com.siemens.nextwork.tag.dto.tags.TagsGetResponseDTO;
import com.siemens.nextwork.tag.dto.tags.TagsRequestDTO;
import com.siemens.nextwork.tag.dto.tags.TagsResponseDTO;
import com.siemens.nextwork.tag.service.TagService;
import com.siemens.nextwork.tag.util.CommonUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/tags")
@Tag(description = "API endpoint for Tag Service", name = "Tag API")
public class TagsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TagService tagService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagsResponseDTO> createNewTag(
            @Valid @RequestBody TagsRequestDTO tagsRequestDTO) throws Exception {
        LOGGER.info("Create tag request received");
        String userEmail = CommonUtils.getEmailId(request.getHeader(NextworkConstants.AUTHORIZATION_HEADER));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.createNewTag(userEmail, tagsRequestDTO));
    }

    @PutMapping(path = "/{tagId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagsResponseDTO> updateTag(@Valid @RequestBody TagsRequestDTO tagsRequestDTO, @PathVariable("tagId") String tagId) throws Exception {
        LOGGER.info("Update tag request received");
        String userEmail = CommonUtils.getEmailId(request.getHeader(NextworkConstants.AUTHORIZATION_HEADER));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagService.updateTag(userEmail, tagsRequestDTO, tagId));
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdsResponseDTO> deleteTags(@RequestParam(value = "wsId") String wsId,
                                                     @RequestParam(required = true, value = "action") String action,
                                                     @RequestBody DeleteTagsDTO deleteTagsDTO) throws Exception {
        LOGGER.info("Delete tag request received");
        String userEmail = CommonUtils.getEmailId(request.getHeader(NextworkConstants.AUTHORIZATION_HEADER));
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(tagService.deleteTags(userEmail, wsId, deleteTagsDTO, action));
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagsGetResponseDTO> getTags(@RequestParam(value = "wsId",required = false) String workStreamId,
                                                      @RequestParam(value = "purpose") String purpose) throws Exception {
        LOGGER.info("Get tag request received");
        String userEmail = CommonUtils.getEmailId(request.getHeader(NextworkConstants.AUTHORIZATION_HEADER));
        return ResponseEntity.status(HttpStatus.OK)
                .body(tagService.getTags(userEmail, workStreamId, purpose));
    }



}
