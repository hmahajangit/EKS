package com.siemens.nextwork.tag.service;


import static org.mockito.Mockito.when;


import com.siemens.nextwork.tag.RestTestEnabler;
import com.siemens.nextwork.tag.TagManagementServiceApplication;
import com.siemens.nextwork.tag.constants.NextworkConstants;
import com.siemens.nextwork.tag.dto.IdsResponseDTO;
import com.siemens.nextwork.tag.dto.tags.DeleteTagsDTO;
import com.siemens.nextwork.tag.dto.tags.TagsGetResponseDTO;
import com.siemens.nextwork.tag.dto.tags.TagsRequestDTO;
import com.siemens.nextwork.tag.dto.tags.TagsResponseDTO;
import com.siemens.nextwork.tag.enums.TagCategory;
import com.siemens.nextwork.tag.enums.TagOriginType;
import com.siemens.nextwork.tag.exception.ResourceNotFoundException;
import com.siemens.nextwork.tag.exception.RestBadRequestException;
import com.siemens.nextwork.tag.exception.RestForbiddenException;
import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Roles;
import com.siemens.nextwork.tag.model.Tags;
import com.siemens.nextwork.tag.model.Workstream;
import com.siemens.nextwork.tag.repo.TagsRepository;
import com.siemens.nextwork.tag.repo.WorkStreamRepository;
import com.siemens.nextwork.tag.service.impl.TagServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


import io.jsonwebtoken.Jwts;

import java.util.*;

@ContextConfiguration(classes = TagManagementServiceApplication.class)
@SpringBootTest
class TagServiceTest extends RestTestEnabler {

    String dummyToken = Jwts.builder().claim("email", "nextwork@siemens.com").compact();
    String authorization = "bearer " + dummyToken;
    Workstream workstream;

    NextWorkUser nextWorkUser;

    String userEmail = "abc@siemens.com";

    List<Roles> roleList;
    Roles role;

    String id = "id01";


    @Mock
    private UserService userService;


    @Mock
    private WorkStreamRepository workStreamRepository;

    @Mock
    private TagsRepository tagRepository;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private TagServiceImpl tagService;


    @Override
    @BeforeEach
    public void setup() {
        when(request.getHeader(Mockito.any())).thenReturn(authorization);
        workstream = new Workstream();
        workstream.setName("workstream_tag");
        workstream.setUid("1234");
        List<String> tagIds = Arrays.asList("124890");
        workstream.setTags(tagIds);
        nextWorkUser = new NextWorkUser();
        roleList = new ArrayList<>();
        role = new Roles();
        role.setRoleType("ADMIN");
        roleList.add(role);
    }

    @Test
    void testToCreateNewGlobalTag() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setOrigin(TagOriginType.TAG_MANAGEMENT.value);
        tagRequestDTO.setCategory(TagCategory.GLOBAL.value);
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        entities.add(NextworkConstants.TAG_ENTITY_JOB_PROFILE);
        entities.add(NextworkConstants.TAG_ENTITY_SKILLS);
        entities.add(NextworkConstants.TAG_ENTITY_MEASURES);
        entities.add(NextworkConstants.TAG_ENTITY_IMPACT);
        entities.add(NextworkConstants.TAG_ENTITY_JOB_CLUSTER);
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        TagsResponseDTO tagsResponseDTO = tagService.createNewTag("siemens@siemens.com", tagRequestDTO);
        Assertions.assertNotNull(tagsResponseDTO);
    }

    @Test
    void testToCreateGlobalTagWhenCategoryInvalid() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory("global");
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.createNewTag("siemens@siemens.com", tagRequestDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToCreateNewGlobalTagForUserRole() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.GLOBAL.value);
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        roleList = new ArrayList<>();
        role.setRoleType("USER");
        roleList.add(role);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestForbiddenException exception = Assertions.assertThrows(RestForbiddenException.class, () -> tagService.createNewTag("siemens@siemens.com", tagRequestDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestForbiddenException.class, exception.getClass());
    }

    @Test
    void testToCreateNewGlobalTagForEmptyRole() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.GLOBAL.value);
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        roleList = new ArrayList<>();
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestForbiddenException exception = Assertions.assertThrows(RestForbiddenException.class, () -> tagService.createNewTag("siemens@siemens.com", tagRequestDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestForbiddenException.class, exception.getClass());
    }

    @Test
    void testToCreateNewLocalTagWenWsIdNull() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.LOCAL.value);
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        roleList = new ArrayList<>();
        role.setRoleType("USER");
        roleList.add(role);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.createNewTag("siemens@siemens.com", tagRequestDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToCreateNewLocalTagForUser() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.LOCAL.value);
        tagRequestDTO.setOrigin(TagOriginType.DIRECT_ASSIGNEMENT.value);
        tagRequestDTO.setWsId("wsId");
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        roleList = new ArrayList<>();
        role.setRoleType("USER");
        roleList.add(role);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(Mockito.anyString(), Mockito.anyString())).thenReturn(workstream);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        TagsResponseDTO tagsResponseDTO = tagService.createNewTag("siemens@siemens.com", tagRequestDTO);
        Assertions.assertNotNull(tagsResponseDTO);
    }

    @Test
    void testToUpdateGlobalTag() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.GLOBAL.value);
        List<String> entities = new ArrayList<>();
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findById(Mockito.anyString())).thenReturn(Optional.of(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        TagsResponseDTO tagsResponseDTO = tagService.updateTag("siemens@siemens.com", tagRequestDTO, "tagId");
        Assertions.assertNotNull(tagsResponseDTO);
    }

    @Test
    void testToUpdateGlobalTagWhenEntitiesEmpty() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.GLOBAL.value);
        List<String> entities = new ArrayList<>();
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findById(Mockito.anyString())).thenReturn(Optional.of(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.updateTag("siemens@siemens.com", tagRequestDTO, "tagId"));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToUpdateGlobalTagWhenEntityNameIsInvalid() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory(TagCategory.GLOBAL.value);
        List<String> entities = new ArrayList<>();
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        entities.add("workstream");
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findById(Mockito.anyString())).thenReturn(Optional.of(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.updateTag("siemens@siemens.com", tagRequestDTO, "tagId"));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToDeleteGlobalTagWhenTagIdInvalid() {
        DeleteTagsDTO deleteTagsDTO = new DeleteTagsDTO();
        List<String> uids = new ArrayList<>();
        uids.add("652e3d4e880f930617e39497");
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        deleteTagsDTO.setUids(uids);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTags("siemens@siemens.com", "wsId", deleteTagsDTO, "Activate"));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(ResourceNotFoundException.class, exception.getClass());
    }

    @Test
    void testToDeleteGlobalTagWhenRequestDTOEmpty() {
        DeleteTagsDTO deleteTagsDTO = new DeleteTagsDTO();
        List<String> uids = new ArrayList<>();
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        deleteTagsDTO.setUids(uids);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.deleteTags("siemens@siemens.com", "wsId", deleteTagsDTO, "Activate"));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToDeleteGlobalTag() {
        DeleteTagsDTO deleteTagsDTO = new DeleteTagsDTO();
        List<String> uids = new ArrayList<>();
        uids.add("124890");
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        deleteTagsDTO.setUids(uids);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        IdsResponseDTO idsResponseDTO = tagService.deleteTags("siemens@siemens.com", "wsId", deleteTagsDTO, "Activate");
        Assertions.assertNotNull(idsResponseDTO);
    }

    @Test
    void testToDeleteLocalTag() {
        DeleteTagsDTO deleteTagsDTO = new DeleteTagsDTO();
        List<String> uids = new ArrayList<>();
        uids.add("124890");
        Tags tag = getGlobalTags(TagCategory.LOCAL.value);
        deleteTagsDTO.setUids(uids);
        roleList = new ArrayList<>();
        role.setRoleType("USER");
        roleList.add(role);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        IdsResponseDTO idsResponseDTO = tagService.deleteTags("siemens@siemens.com", "wsId", deleteTagsDTO, "Delete");
        Assertions.assertNotNull(idsResponseDTO);
    }

    @Test
    void testToCreateGlobalTagWhenNameIsExists() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName("tag1");
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory("global");
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.createNewTag("siemens@siemens.com", tagRequestDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToCreateGlobalTagWhenNameIsEmpty() {
        TagsRequestDTO tagRequestDTO = new TagsRequestDTO();
        tagRequestDTO.setName(null);
        tagRequestDTO.setDescription("description");
        tagRequestDTO.setCategory("global");
        List<String> entities = new ArrayList<>();
        entities.add(NextworkConstants.TAG_ENTITY_WORK_STREAM);
        tagRequestDTO.setTagEntities(entities);
        nextWorkUser.setRolesDetails(roleList);
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.createNewTag("siemens@siemens.com", tagRequestDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToGetAllTagForSearch() {
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(Mockito.anyString(), Mockito.anyString())).thenReturn(workstream);
        TagsGetResponseDTO tagsResponseDTO = tagService.getTags("siemens@siemens.com", "wsId", "Search");
        Assertions.assertNotNull(tagsResponseDTO);
    }

    @Test
    void testToGetAllTagForInvalidPurpose() {
        Tags tag = getGlobalTags(TagCategory.GLOBAL.value);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByNameAndCategory(Mockito.anyString(), Mockito.anyString())).thenReturn(Collections.emptyList());
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(Mockito.anyString(), Mockito.anyString())).thenReturn(workstream);
        RestBadRequestException exception = Assertions.assertThrows(RestBadRequestException.class, () -> tagService.getTags("siemens@siemens.com", "wsId", "SearchTest"));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(RestBadRequestException.class, exception.getClass());
    }

    @Test
    void testToGetAllTagForList() {
        Tags tag = getGlobalTags(TagCategory.LOCAL.value);
        roleList = new ArrayList<>();
        role.setRoleType("USER");
        roleList.add(role);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByTagsIds(Mockito.anyList())).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(Mockito.anyString(), Mockito.anyString())).thenReturn(workstream);
        TagsGetResponseDTO tagsResponseDTO = tagService.getTags("siemens@siemens.com", "wsId", "List");
        Assertions.assertNotNull(tagsResponseDTO);
    }

    @Test
    void testToGetAllTheGlobalTagsForAdmin() {
        Tags tag = getGlobalTags(TagCategory.LOCAL.value);
        nextWorkUser.setRolesDetails(roleList);
        when(userService.getUserByEmail(Mockito.anyString())).thenReturn(nextWorkUser);
        when(tagRepository.findByTagsIds(Mockito.anyList())).thenReturn(Collections.singletonList(tag));
        when(userService.findByEmail(Mockito.any())).thenReturn(id);
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tag));
        when(userService.checkWorkStreamMemberOwnerUnPublishedWorkstream(Mockito.anyString(), Mockito.anyString())).thenReturn(workstream);
        when(tagRepository.findAllGlobalTags()).thenReturn(Collections.singletonList(tag));
        TagsGetResponseDTO tagsResponseDTO = tagService.getTags("siemens@siemens.com", null, "List");
        Assertions.assertNotNull(tagsResponseDTO);
    }

    public Tags getGlobalTags(String category) {
        Tags tag = new Tags();
        List<String> entities = new ArrayList<>();
        entities.add("workstream");
        tag.setId("124890");
        tag.setName("tag1");
        tag.setDescription("description");
        tag.setCategory(category);
        tag.setTagEntities(entities);
        return tag;
    }

}
