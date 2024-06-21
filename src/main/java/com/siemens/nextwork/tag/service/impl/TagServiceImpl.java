package com.siemens.nextwork.tag.service.impl;


import com.siemens.nextwork.tag.constants.NextworkConstants;
import com.siemens.nextwork.tag.dto.IdsResponseDTO;
import com.siemens.nextwork.tag.dto.tags.DeleteTagsDTO;
import com.siemens.nextwork.tag.dto.tags.TagsGetResponseDTO;
import com.siemens.nextwork.tag.dto.tags.TagsRequestDTO;
import com.siemens.nextwork.tag.dto.tags.TagsResponseDTO;
import com.siemens.nextwork.tag.enums.TagCategory;
import com.siemens.nextwork.tag.enums.TagEntityType;
import com.siemens.nextwork.tag.enums.TagOriginType;
import com.siemens.nextwork.tag.exception.ResourceNotFoundException;
import com.siemens.nextwork.tag.exception.RestBadRequestException;
import com.siemens.nextwork.tag.exception.RestForbiddenException;
import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Tags;
import com.siemens.nextwork.tag.model.Workstream;
import com.siemens.nextwork.tag.repo.NextWorkUserRepository;
import com.siemens.nextwork.tag.repo.TagsRepository;
import com.siemens.nextwork.tag.repo.WorkStreamRepository;
import com.siemens.nextwork.tag.service.TagService;
import com.siemens.nextwork.tag.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);
    @Autowired
    private WorkStreamRepository workStreamRepository;
    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private NextWorkUserRepository nextWorkUserRepository;
    @Autowired
    private UserService userService;


    @Override
    public TagsResponseDTO createNewTag(String userEmail, TagsRequestDTO tagRequestDTO) {
        LOGGER.info("Inside the create tag");
        validateRoleForGlobalTagCreation(tagRequestDTO.getCategory(), userEmail);
        validateRoleForLocalTagCreation(tagRequestDTO.getCategory(), tagRequestDTO.getWsId(), userEmail);
        validateTagName(tagRequestDTO);
        String userId = userService.findByEmail(userEmail);
        Tags tags = convertTagDtoToEntityCreate(tagRequestDTO, userId);
        return buildTagsResponseDTOCreateAndUpdate(tags, NextworkConstants.CREATE);
    }

    @Override
    public TagsResponseDTO updateTag(String userEmail, TagsRequestDTO tagRequestDTO, String tagId) {
        LOGGER.info("Inside the update tag");
        Tags tag = tagsRepository.findById(tagId).orElseThrow(() -> new RestBadRequestException("Tag not found"));
        validateTagName(tagRequestDTO);
        validateRoleForGlobalTagCreation(tag.getCategory(), userEmail);
        validateRoleForLocalTagCreation(tag.getCategory(), tagRequestDTO.getWsId(), userEmail);
        String userId = userService.findByEmail(userEmail);
        Tags tagsUpdated = convertTagDtoToEntityUpdate(tag, tagRequestDTO, userId);
        return buildTagsResponseDTOCreateAndUpdate(tagsUpdated, NextworkConstants.UPDATE);
    }

    @Override
    public IdsResponseDTO deleteTags(String userEmail, String workStreamId, DeleteTagsDTO deleteTagsDTO, String action) {
        LOGGER.info("Inside the delete tag");
        validateRoleForDeleteTags(action, userEmail, workStreamId);
        List<Tags> tags = tagsRepository.findAll();
        validateTagIdExist(deleteTagsDTO, tags);
        if (!CollectionUtils.isEmpty(deleteTagsDTO.getUids())) {
            List<String> deletedTagIds = new ArrayList<>();
            IdsResponseDTO idsResponseDTO = new IdsResponseDTO();
            for (Tags tag : tags) {
                if (deleteTagsDTO.getUids().contains(tag.getId())) {
                    if (action.equals(NextworkConstants.TAG_ACTIVATION) || action.equals(NextworkConstants.TAG_DEACTIVATION)) {
                        tag.setIsActive(action.equals(NextworkConstants.TAG_ACTIVATION));
                    } else if (action.equals(NextworkConstants.TAG_DELETE)) {
                        tag.setIsDeleted(true);
                    }
                    deletedTagIds.add(tag.getId());

                }
            }
            tagsRepository.saveAll(tags);
            idsResponseDTO.setUids(deletedTagIds);
            return idsResponseDTO;
        } else {
            throw new RestBadRequestException("Tag Ids required");
        }
    }

    public void validateRoleForDeleteTags(String action, String userEmail, String wsId) {
        if (action.equals(NextworkConstants.TAG_ACTIVATION) || action.equals(NextworkConstants.TAG_DEACTIVATION)) {
            validateRoleForGlobalTagCreation(TagCategory.GLOBAL.value, userEmail);
        } else if (action.equals(NextworkConstants.TAG_DELETE)) {
            validateRoleForLocalTagCreation(TagCategory.LOCAL.value, wsId, userEmail);
        }
    }

    private Tags convertTagDtoToEntityUpdate(Tags tags, TagsRequestDTO tagRequestDTO, String userId) {
        tags.setName(tagRequestDTO.getName());
        tags.setDescription(tagRequestDTO.getDescription());
        tags.setWsId(Objects.nonNull(tagRequestDTO.getWsId()) ? tagRequestDTO.getWsId() : null);
        if (!getTagEntities(tagRequestDTO.getTagEntities()).isEmpty()) {
            tags.setTagEntities(getTagEntities(tagRequestDTO.getTagEntities()));
        }
        tags.setUpdatedBy(userId);
        tags.setUpdatedOn(new Date());
        tagsRepository.save(tags);
        return tags;
    }

    private TagsResponseDTO buildTagsResponseDTOCreateAndUpdate(Tags tags, String operation) {
        TagsResponseDTO tagsResponseDTO = new TagsResponseDTO();
        tagsResponseDTO.setName(tags.getName());
        tagsResponseDTO.setDescription(tags.getDescription());
        tagsResponseDTO.setWsId(tags.getWsId());
        tagsResponseDTO.setTagEntities(tags.getTagEntities());
        if (operation.equalsIgnoreCase(NextworkConstants.CREATE)) {
            tagsResponseDTO.setId(tags.getId());
            tagsResponseDTO.setPlaceOfCreation(tags.getOrigin());
            tagsResponseDTO.setCategory(tags.getCategory());
            tagsResponseDTO.setCreatedBy(tags.getCreatedBy());
            tagsResponseDTO.setCreatedOn(tags.getCreatedOn());
        } else {
            tagsResponseDTO.setUpdatedBy(tags.getUpdatedBy());
            tagsResponseDTO.setUpdatedOn(tags.getUpdatedOn());
        }
        return tagsResponseDTO;
    }

    private Tags convertTagDtoToEntityCreate(TagsRequestDTO tagRequestDTO, String userId) {
        Tags tags = new Tags();
        tags.setId(new ObjectId().toString());
        tags.setCategory(getTagCategory(tagRequestDTO.getCategory()));
        tags.setCreatedOn(new Date());
        tags.setCreatedBy(userId);
        tags.setName(tagRequestDTO.getName());
        tags.setDescription(tagRequestDTO.getDescription());
        tags.setWsId(Objects.nonNull(tagRequestDTO.getWsId()) ? tagRequestDTO.getWsId() : null);
        tags.setTagEntities(getTagEntities(tagRequestDTO.getTagEntities()));
        tags.setOrigin(getTagOriginType(tagRequestDTO.getOrigin()));
        tagsRepository.save(tags);
        return tags;
    }


    private String getTagCategory(String tagCategory) {
        return switch (tagCategory) {
            case NextworkConstants.GLOBAL_TAG -> TagCategory.GLOBAL.value;
            case NextworkConstants.LOCAL_TAG -> TagCategory.LOCAL.value;
            default -> throw new RestBadRequestException(NextworkConstants.INVALID_TAG_CATEGORY);
        };
    }

    private String getTagOriginType(String originType) {
        return switch (originType) {
            case NextworkConstants.TAG_MANAGEMENT -> TagOriginType.TAG_MANAGEMENT.value;
            case NextworkConstants.DIRECT_ASSIGNMENT -> TagOriginType.DIRECT_ASSIGNEMENT.value;
            default -> throw new RestBadRequestException(NextworkConstants.INVALID_TAG_ORIGIN);
        };
    }

    private List<String> getTagEntities(List<String> tagEntities) {
        List<String> entities = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(tagEntities)) {
            tagEntities.forEach(entity -> entities.add(validateTagEntity(entity)));
        } else {
            throw new RestBadRequestException("Tag entities should not be empty");
        }
        return entities;
    }

    public String validateTagEntity(String tagEntity) {
        return switch (tagEntity) {
            case NextworkConstants.TAG_ENTITY_WORK_STREAM -> TagEntityType.WORKSTREAM.value;
            case NextworkConstants.TAG_ENTITY_JOB_PROFILE -> TagEntityType.JOBPROFILE.value;
            case NextworkConstants.TAG_ENTITY_SKILLS -> TagEntityType.SKILLS.value;
            case NextworkConstants.TAG_ENTITY_IMPACT -> TagEntityType.IMAPACT.value;
            case NextworkConstants.TAG_ENTITY_MEASURES -> TagEntityType.MEASURES.value;
            case NextworkConstants.TAG_ENTITY_JOB_CLUSTER -> TagEntityType.JOBCLUSTER.value;
            default -> throw new RestBadRequestException(NextworkConstants.INVALID_TAG_ENTITIES);
        };
    }

    public void validateTagName(TagsRequestDTO tagsRequestDTO) {
        if (StringUtils.isNotEmpty(tagsRequestDTO.getName())) {
            List<Tags> tagList = tagsRepository.findByNameAndCategory(tagsRequestDTO.getName(), tagsRequestDTO.getCategory());
            int count = tagList.size();
            if (count > 0) {
                throw new RestBadRequestException("Tag Name is already exist");
            }
        } else {
            throw new RestBadRequestException("Tag name should not be empty");
        }
    }

    public void validateRoleForGlobalTagCreation(String tagCategory, String userEmail) {
        NextWorkUser userOpt = userService.getUserByEmail(userEmail);
        if (null != userOpt.getRolesDetails() && !userOpt.getRolesDetails().isEmpty()) {
            String userRole = userOpt.getRolesDetails().get(0).getRoleType();
            if (tagCategory.equals(NextworkConstants.GLOBAL_TAG) && !userRole.equals(NextworkConstants.ADMIN)) {
                throw new RestForbiddenException("Only Admin can able to create/update/activate/deactivate the global tags");
            }
        } else {
            throw new RestForbiddenException("User doesn't have any role assigned.");
        }
    }

    public void validateRoleForLocalTagCreation(String tagCategory, String wsId, String userEmail) {
        if (tagCategory.equals(TagCategory.LOCAL.value)) {
            if (StringUtils.isEmpty(wsId)) {
                throw new RestBadRequestException("Work stream id is required for local tags");
            }
            userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(wsId, userEmail);
        }
    }

    @Override
    public TagsGetResponseDTO getTags(String userEmail, String workStreamId, String purpose) {
        LOGGER.info("Inside the get tag");
        NextWorkUser user = userService.getUserByEmail(userEmail);
        validatePurpose(purpose);
        TagsGetResponseDTO tagsResponseDTO = new TagsGetResponseDTO();
        if (purpose.equalsIgnoreCase("List")) {
            if (StringUtils.isNotEmpty(workStreamId) && !user.getRolesDetails().get(0).getRoleType().equals(NextworkConstants.ADMIN)) {
                Workstream ws = userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(workStreamId, userEmail);
                Map<String, Map<String, Set<TagsGetResponseDTO.Association>>> associationTypeMap = new HashMap<>();
                Set<String>tagsIdsList = new HashSet<>();
                setAssociationMap(associationTypeMap,ws,tagsIdsList);
                List<Tags> tags = tagsRepository.findByTagsIds(new ArrayList<>(tagsIdsList));
                List<Tags> allLocalTags = tagsRepository.findAllLocalTagsByWsId(workStreamId);
                Set<Tags> allTagsSet = new HashSet<>(allLocalTags);
                allTagsSet.addAll(tags);
                List<TagsGetResponseDTO.TagsDTO> listDataList = new ArrayList<>();
                allTagsSet.forEach(tag -> setListData(tag, listDataList, associationTypeMap));
                tagsResponseDTO.setListData(listDataList);
            }
            else if(StringUtils.isEmpty(workStreamId) && user.getRolesDetails().get(0).getRoleType().equals(NextworkConstants.ADMIN)) {
                    List<Tags> globalTags = tagsRepository.findAllGlobalTags();
                    if (!CollectionUtils.isEmpty(globalTags)) {
                        List<TagsGetResponseDTO.TagsDTO> listDataList = new ArrayList<>();
                        globalTags.forEach(tag -> setListData(tag, listDataList, new HashMap<>()));
                        tagsResponseDTO.setListData(listDataList);
                    }
                }
        }  else {
                List<TagsGetResponseDTO.SearchData> globalTags = tagsRepository.findGlobalTagsData();
                List<TagsGetResponseDTO.SearchData> localTags = tagsRepository.findLocalTagsDataByWsId(workStreamId);
                List<TagsGetResponseDTO.SearchData> searchDataList = Stream.of(globalTags, localTags).flatMap(Collection::stream).toList();
                if (!CollectionUtils.isEmpty(searchDataList)) {
                    tagsResponseDTO.setSearchData(searchDataList);
                }
            }
        return tagsResponseDTO;
    }

    private void setAssociationMap(Map<String, Map<String, Set<TagsGetResponseDTO.Association>>> associationTypeMap, Workstream ws, Set<String> tagsIdsList){
        Map<String, Set<TagsGetResponseDTO.Association>> associationWorkStreamMap= Optional.ofNullable(ws.getTags()).orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(Function.identity(),id->Set.of(
                        new TagsGetResponseDTO.Association(TagEntityType.WORKSTREAM.value,ws.getName(),ws.getUid(), false))));
        tagsIdsList.addAll(associationWorkStreamMap.keySet());
        associationTypeMap.put(TagEntityType.WORKSTREAM.value, associationWorkStreamMap);

        Map<String, Set<TagsGetResponseDTO.Association>> associationJobProfileMap =
                Optional.ofNullable(ws.getJobProfiles())
                        .orElse(new ArrayList<>())
                        .stream()
                        .flatMap(jobProfile ->
                                Optional.ofNullable(jobProfile.getTags())
                                        .orElse(new ArrayList<>())
                                        .stream()
                                        .map(id -> new AbstractMap.SimpleEntry<>(id, jobProfile))
                        )
                        .collect(Collectors.toMap(
                                AbstractMap.SimpleEntry::getKey,
                                entry -> {
                                    TagsGetResponseDTO.Association association = new TagsGetResponseDTO.Association(
                                            TagEntityType.JOBPROFILE.value,
                                            entry.getValue().getName(),
                                            entry.getValue().getUid(),
                                            entry.getValue().getIsOriginFuture()
                                    );
                                    Set<TagsGetResponseDTO.Association> associationsSet = new HashSet<>();
                                    associationsSet.add(association);
                                    return associationsSet;
                                },
                                (existingSet, newSet) -> {
                                    existingSet.addAll(newSet);
                                    return existingSet;
                                }
                        ));
        tagsIdsList.addAll(associationJobProfileMap.keySet());
        associationTypeMap.put(TagEntityType.JOBPROFILE.value, associationJobProfileMap);

        Map<String, Set<TagsGetResponseDTO.Association>> associationSkillMap= Optional.ofNullable(ws.getSkills()).orElse(new ArrayList<>()).stream()
                .flatMap(measure-> Optional.ofNullable(measure.getTags()).orElse(new ArrayList<>()).stream().map(id->new AbstractMap.SimpleEntry<>(id,measure)))
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        entry -> {
                            TagsGetResponseDTO.Association association = new TagsGetResponseDTO.Association(
                                    TagEntityType.SKILLS.value,
                                    entry.getValue().getName(),
                                    entry.getValue().getUid(),
                                    false
                            );
                            Set<TagsGetResponseDTO.Association> associations = new HashSet<>();
                            associations.add(association);
                            return associations;
                        },
                        (existingSet, newSet) -> {
                            existingSet.addAll(newSet);
                            return existingSet;
                        }
                ));
        tagsIdsList.addAll(associationSkillMap.keySet());
        associationTypeMap.put(TagEntityType.SKILLS.value, associationSkillMap);

        Map<String, Set<TagsGetResponseDTO.Association>> associationMeasureMap= Optional.ofNullable(ws.getLatestMatrixDetails()).orElse(new ArrayList<>()).stream().map(latestMatrixModel -> Optional.ofNullable(latestMatrixModel.getMeasures()).orElse(new ArrayList<>())).flatMap(Collection::stream)
                .flatMap(measure-> Optional.ofNullable(measure.getTags()).orElse(new ArrayList<>()).stream().map(id->new AbstractMap.SimpleEntry<>(id,measure)))
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        entry -> {
                            TagsGetResponseDTO.Association association = new TagsGetResponseDTO.Association(
                                    TagEntityType.MEASURES.value,
                                    entry.getValue().getMeasuresType().name(),
                                    entry.getValue().getUid(),
                                    false
                            );
                            Set<TagsGetResponseDTO.Association> associations = new HashSet<>();
                            associations.add(association);
                            return associations;
                        },
                        (existingSet, newSet) -> {
                            existingSet.addAll(newSet);
                            return existingSet;
                        }
                ));
        tagsIdsList.addAll(associationMeasureMap.keySet());
        associationTypeMap.put(TagEntityType.MEASURES.value, associationMeasureMap);

        Map<String,Set<TagsGetResponseDTO.Association>> associationTrendsMeasureMap=Optional.ofNullable(ws.getTrends()).orElse(new ArrayList<>()).stream()
                .flatMap(trend-> Optional.ofNullable(trend.getTags()).orElse(new ArrayList<>()).stream().map(id->new AbstractMap.SimpleEntry<>(id,trend)))
                .collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        entry -> {
                            TagsGetResponseDTO.Association association = new TagsGetResponseDTO.Association(
                                    TagEntityType.IMAPACT.value,
                                    entry.getValue().getImpactType(),
                                    entry.getValue().getUid(),
                                    false
                            );
                            Set<TagsGetResponseDTO.Association> associations = new HashSet<>();
                            associations.add(association);
                            return associations;
                        },
                        (existingSet, newSet) -> {
                            existingSet.addAll(newSet);
                            return existingSet;
                        }
                ));
        tagsIdsList.addAll(associationTrendsMeasureMap.keySet());
        associationTypeMap.put(TagEntityType.IMAPACT.value, associationTrendsMeasureMap);
    }
    public void setListData(Tags tag, List<TagsGetResponseDTO.TagsDTO> listDataList, Map<String, Map<String, Set<TagsGetResponseDTO.Association>>> associationTypeMap){
        TagsGetResponseDTO.TagsDTO tagData = new TagsGetResponseDTO.TagsDTO();
        tagData.setName(tag.getName());
        tagData.setDescription(tag.getDescription());
        tagData.setIsActive(tag.getIsActive());
        tagData.setOrigin(tag.getOrigin());
        tagData.setCategory(tag.getCategory());
        tagData.setTagEntities(tag.getTagEntities());
        List<TagsGetResponseDTO.Association> associationList = new ArrayList<>();
        Optional.ofNullable(tag.getTagEntities()).orElse(new ArrayList<>()).forEach(tagEntity -> {
            if (associationTypeMap.containsKey(tagEntity) && associationTypeMap.get(tagEntity).containsKey(tag.getId())) {
                associationList.addAll(associationTypeMap.get(tagEntity).get(tag.getId()));
            }
        });
        tagData.setAssociations(associationList);
        listDataList.add(tagData);
    }

    private void validatePurpose(String purpose) {
        if (!purpose.equalsIgnoreCase("List") && !purpose.equalsIgnoreCase("Search")) {
            throw new RestBadRequestException("Invalid purpose");
        }
    }
    private void validateTagIdExist(DeleteTagsDTO deleteTagsDTO, List<Tags> tags) {
        for (String tagId : deleteTagsDTO.getUids()) {
            if (tags.stream().noneMatch(tag -> tag.getId().equals(tagId))) {
                throw new ResourceNotFoundException("Given TagId is not found :" + tagId);
            }
        }

    }
}
