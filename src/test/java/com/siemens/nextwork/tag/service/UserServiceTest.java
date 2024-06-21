package com.siemens.nextwork.tag.service;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.siemens.nextwork.tag.RestTestEnabler;
import com.siemens.nextwork.tag.TagManagementServiceApplication;
import com.siemens.nextwork.tag.enums.PublishType;
import com.siemens.nextwork.tag.exception.ResourceNotFoundException;
import com.siemens.nextwork.tag.exception.RestForbiddenException;
import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Roles;
import com.siemens.nextwork.tag.model.Workstream;
import com.siemens.nextwork.tag.repo.NextWorkUserRepository;
import com.siemens.nextwork.tag.repo.WorkStreamRepository;
import com.siemens.nextwork.tag.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ContextConfiguration;

import com.siemens.nextwork.tag.dto.WorkStreamDTO;


@ContextConfiguration(classes = TagManagementServiceApplication.class)
@SpringBootTest
class UserServiceTest extends RestTestEnabler {

    @Mock
    private NextWorkUserRepository nextWorkUserRepository;

    @Mock
    private WorkStreamRepository workStreamRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    String userEmail = "abc@siemens.com";
    String userId = "u001";
    NextWorkUser user;
    Optional<NextWorkUser> userOpt;
    List<Roles> roleList;
    Roles role;
    String wsId ="ws001";
    Workstream workStream;
    Optional<Workstream> workStreamOptional;



    @BeforeEach
    public void setup() {
        user = new NextWorkUser();
        user.setStatus("Active");
        roleList = new ArrayList<>();
        role = new Roles();
        role.setRoleType("ADMIN");
        roleList.add(role);
        user.setRolesDetails(roleList);
        List<WorkStreamDTO> workStreamDTOList = new ArrayList<>();
        WorkStreamDTO workStreamDTO = new WorkStreamDTO();
        workStreamDTO.setUid(wsId);
        workStreamDTOList.add(workStreamDTO);
        user.setWorkStreamList(workStreamDTOList);
        userOpt = Optional.of(user);
        workStream = new Workstream();
        workStream.setPublishedStatus(PublishType.PUBLISH);
        workStreamOptional = Optional.of(workStream);


    }

    @Test
    void findByEmailTest() throws Exception {
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        Assertions.assertNull(userServiceImpl.findByEmail(userEmail));

    }

    @Test
    void findByEmailUserNotActiveTest() throws Exception {
        user.setStatus("Deactive");
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.findByEmail(userEmail));

    }

    @Test
    void getUserByEmailTest() throws Exception {
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        Assertions.assertNotNull(userServiceImpl.getUserByEmail(userEmail));

    }

    @Test
    void getUserByEmailNotActiveTest() throws Exception {
        user.setStatus("Deactive");
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUserByEmail(userEmail));

    }

    @Test
    void getUserRoleByEmailTest() throws Exception {
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        Assertions.assertNotNull(userServiceImpl.getUserRoleByEmail(userEmail));

    }

    @Test
    void getUserRoleByEmailUserNotActiveTest() throws Exception {
        user.setStatus("Deactive");
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUserRoleByEmail(userEmail));

    }

    @Test
    void testCheckWorkStreamMemberOwnerUnPublishedWorkstreamEmpty(){

        user.getRolesDetails().get(0).setRoleType("USER");
        user.getWorkStreamList().get(0).setUid(userId);
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        when(workStreamRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.checkWorkStreamMemberOwnerUnPublishedWorkstream(wsId, userEmail));

    }
    @Test
    void testCheckWorkStreamMemberOwnerUnPublishedWorkstreamPublishFails(){
        workStream.setPublishedStatus(PublishType.PUBLISH);
        user.getRolesDetails().get(0).setRoleType("USER");
        user.getWorkStreamList().get(0).setUid(userId);
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        when(workStreamRepository.findById(Mockito.anyString())).thenReturn(workStreamOptional);
        Assertions.assertThrows(RestForbiddenException.class, () -> userServiceImpl.checkWorkStreamMemberOwnerUnPublishedWorkstream(wsId, userEmail));

    }
    @Test
    void testCheckWorkStreamMemberOwnerUnPublishedWorkstreamAdminFails(){
        user.getRolesDetails().get(0).setRoleType("ADMIN");
        user.getWorkStreamList().get(0).setUid(userId);
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        when(workStreamRepository.findById(Mockito.anyString())).thenReturn(workStreamOptional);
        Assertions.assertThrows(RestForbiddenException.class, () -> userServiceImpl.checkWorkStreamMemberOwnerUnPublishedWorkstream(wsId, userEmail));

    }
    @Test
    void testCheckWorkStreamMemberOwnerUnPublishedWorkstreamLocalAdminFails(){
        workStream.setPublishedStatus(PublishType.UNPUBLISH);
        workStreamOptional=Optional.of(workStream);
        user.getRolesDetails().get(0).setRoleType("USER");
        user.getWorkStreamList().get(0).setUid("123");
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(userOpt);
        when(workStreamRepository.findById(Mockito.anyString())).thenReturn(workStreamOptional);
        Assertions.assertThrows(RestForbiddenException.class, () -> userServiceImpl.checkWorkStreamMemberOwnerUnPublishedWorkstream(wsId, userEmail));

    }
    @Test
    void testCheckWorkStreamMemberOwnerUnPublishedWorkstreamUserNotPresent(){
        workStream.setPublishedStatus(PublishType.UNPUBLISH);
        workStreamOptional=Optional.of(workStream);
        user.getRolesDetails().get(0).setRoleType("USER");
        user.getWorkStreamList().get(0).setUid("123");
        when(nextWorkUserRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        when(workStreamRepository.findById(Mockito.anyString())).thenReturn(workStreamOptional);
        Assertions.assertThrows(RestForbiddenException.class, () -> userServiceImpl.checkWorkStreamMemberOwnerUnPublishedWorkstream(wsId, userEmail));

    }




}
