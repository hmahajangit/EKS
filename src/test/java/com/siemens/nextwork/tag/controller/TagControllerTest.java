package com.siemens.nextwork.tag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.siemens.nextwork.tag.RestTestEnabler;
import com.siemens.nextwork.tag.TagManagementServiceApplication;
import com.siemens.nextwork.tag.dto.IdsResponseDTO;
import com.siemens.nextwork.tag.dto.tags.DeleteTagsDTO;
import com.siemens.nextwork.tag.dto.tags.TagsRequestDTO;
import com.siemens.nextwork.tag.dto.WorkStreamDTO;
import com.siemens.nextwork.tag.enums.PublishType;
import com.siemens.nextwork.tag.enums.TagOriginType;
import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Roles;
import com.siemens.nextwork.tag.model.Tags;
import com.siemens.nextwork.tag.model.Workstream;
import com.siemens.nextwork.tag.repo.NextWorkUserRepository;
import com.siemens.nextwork.tag.repo.TagsRepository;
import com.siemens.nextwork.tag.repo.WorkStreamRepository;
import com.siemens.nextwork.tag.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;


import io.jsonwebtoken.Jwts;
import org.springframework.test.web.servlet.MvcResult;

@ContextConfiguration(classes = TagManagementServiceApplication.class)
class TagControllerTest extends RestTestEnabler {


    String email = "siemens@siemens.com";
    String token = Jwts.builder().claim("email", email).compact();
    String authorization = "bearer " + token;
    @MockBean
    private TagsRepository tagRepository;
    @MockBean
    private NextWorkUserRepository nextWorkUserRepository;
    @MockBean
    private WorkStreamRepository workStreamRepository;
    @Autowired
    private UserService userService;
    @MockBean
   private DeleteTagsDTO deleteTagsDTO;

    IdsResponseDTO idResponseDTO;
    Optional<NextWorkUser> user;
    Optional<Workstream> workStreamOptional;
   List<Roles> rolesList;
    List<Tags> tagsList;

    @BeforeEach
    public void setup() {
        List<Roles> roles = new ArrayList<>();
        Roles role = Roles.builder().id("64a52b534febce0ce703981b").roleDisplayName("Admin").roleType("ADMIN")
                .haveGIDList(false).isDeleted(false).build();
        roles.add(role);
        NextWorkUser nxtUser = NextWorkUser.builder().id("64b0e557e3280a909b3a3961").name("PRIYANKA SAXENA")
                .email("siemens@siemens.com").orgCode("ADV GD DEV SE DH DT1")
                .creationDate("2022-12-12 11:17:36.279+00").status("Active").rolesDetails(roles)
                .build();
        workStreamOptional=Optional.of(Workstream.builder().uid("1234").name("workstream1").isDeleted(false).publishedStatus(PublishType.UNPUBLISH).build());
        user = Optional.of(nxtUser);
        rolesList = new ArrayList<>();
        Roles localAdminRole = new Roles();
        localAdminRole.setId("r01");
        localAdminRole.setIsDeleted(false);
        localAdminRole.setRoleType("LOCAL ADMIN");
        idResponseDTO = new IdsResponseDTO();
        idResponseDTO.setUids(Collections.singletonList("1234"));
       Tags tag= new Tags();
        tag.setId("1234");
        tag.setDescription("rtrtt");
        tag.setName("tag1");
        tag.setIsDeleted(false);
         tagsList = new ArrayList<>();
        tagsList.add(tag);

    }
    @Test
    void createNewTagTest() throws Exception {
        TagsRequestDTO tagsRequestDTO=new TagsRequestDTO();
        tagsRequestDTO.setWsId("1234");
        tagsRequestDTO.setDescription("rtrtt");
        tagsRequestDTO.setName("tag1");
        tagsRequestDTO.setTagEntities(Collections.singletonList("JobProfile"));
        tagsRequestDTO.setCategory("Global");
        tagsRequestDTO.setOrigin(TagOriginType.TAG_MANAGEMENT.value);
        when(tagRepository.save(any())).thenReturn(new Tags());
        when(nextWorkUserRepository.findByEmail(email)).thenReturn(user);
        mockMvc.perform(post("/api/v1/tags").header("Authorization", authorization).
                        content(new ObjectMapper().writeValueAsString(tagsRequestDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
    }
    @Test
    void deleteRolesTest() throws Exception {
        WorkStreamDTO workStreamDTO=new WorkStreamDTO();
        workStreamDTO.setUid("1234");
        workStreamDTO.setName("workstream1");
        workStreamDTO.setPublishedStatus("UNPUBLISH");
        workStreamDTO.setOrgCodes(Collections.singletonList("ADV GD DEV SE DH DT1"));
        workStreamDTO.setGidList(Collections.singletonList("1234"));
        NextWorkUser nextWorkUser=user.get();
               nextWorkUser.setWorkStreamList(Collections.singletonList(workStreamDTO));
               Roles roles=new Roles();
               roles.setRoleType("USER");
               nextWorkUser.setRolesDetails(Collections.singletonList(roles));

        DeleteTagsDTO deleteTagsDTO=new DeleteTagsDTO();
        deleteTagsDTO.setUids(Collections.singletonList("1234"));
        when(tagRepository.findAll()).thenReturn(tagsList);
        when(workStreamRepository.findById(any())).thenReturn(workStreamOptional);
        when(nextWorkUserRepository.findByEmail(email)).thenReturn(user);
        MvcResult mvcResult = mockMvc.perform(patch("/api/v1/tags").param("wsId","1234").param("action","Delete").header("Authorization", authorization).
                        content(new ObjectMapper().writeValueAsString(deleteTagsDTO)).contentType(MediaType.APPLICATION_JSON)).
        andExpect(status().isAccepted()).andReturn();

    }
    @Test
     void updateTagTest() throws Exception {
        WorkStreamDTO workStreamDTO=new WorkStreamDTO();
        workStreamDTO.setUid("1234");
        workStreamDTO.setName("workstream1");
        workStreamDTO.setPublishedStatus("UNPUBLISH");
        workStreamDTO.setOrgCodes(Collections.singletonList("ADV GD DEV SE DH DT1"));
        workStreamDTO.setGidList(Collections.singletonList("1234"));
        NextWorkUser nextWorkUser=user.get();
        nextWorkUser.setWorkStreamList(Collections.singletonList(workStreamDTO));
        Roles roles=new Roles();
        roles.setRoleType("USER");
        nextWorkUser.setRolesDetails(Collections.singletonList(roles));
        Tags tag= new Tags();
        tag.setId("1234");
        tag.setDescription("rtrtt");
        tag.setName("tag1");
        tag.setCategory("Local");
        TagsRequestDTO tagsRequestDTO=new TagsRequestDTO();
        tagsRequestDTO.setWsId("1234");
        tagsRequestDTO.setDescription("rtrtt");
        tagsRequestDTO.setName("tag1");
        tagsRequestDTO.setTagEntities(Collections.singletonList("JobProfile"));
        when(tagRepository.findById(any())).thenReturn(Optional.of(tag));
        when(tagRepository.save(any())).thenReturn(tag);
        when(nextWorkUserRepository.findByEmail(email)).thenReturn(user);
        when(workStreamRepository.findById(any())).thenReturn(workStreamOptional);
        mockMvc.perform(put("/api/v1/tags/1234").header("Authorization", authorization).
                        content(new ObjectMapper().writeValueAsString(tagsRequestDTO)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
    }
    @Test
    void getTagsTest() throws Exception {
        WorkStreamDTO workStreamDTO=new WorkStreamDTO();
        workStreamDTO.setUid("1234");
        workStreamDTO.setName("workstream1");
        workStreamDTO.setPublishedStatus("UNPUBLISH");
        workStreamDTO.setOrgCodes(Collections.singletonList("ADV GD DEV SE DH DT1"));
        workStreamDTO.setGidList(Collections.singletonList("1234"));
        NextWorkUser nextWorkUser=user.get();
        nextWorkUser.setWorkStreamList(Collections.singletonList(workStreamDTO));
        Roles roles=new Roles();
        roles.setRoleType("USER");
        nextWorkUser.setRolesDetails(Collections.singletonList(roles));
        when(tagRepository.findAll()).thenReturn(tagsList);
        when(workStreamRepository.findById(any())).thenReturn(workStreamOptional);
        when(nextWorkUserRepository.findByEmail(email)).thenReturn(user);
        mockMvc.perform(get("/api/v1/tags").param("wsId","1234").param("purpose","list").header("Authorization", authorization).
                contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }


}