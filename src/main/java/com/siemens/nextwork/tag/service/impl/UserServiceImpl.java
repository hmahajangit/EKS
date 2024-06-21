package com.siemens.nextwork.tag.service.impl;

import com.siemens.nextwork.tag.constants.NextworkConstants;
import com.siemens.nextwork.tag.dto.WorkStreamDTO;
import com.siemens.nextwork.tag.enums.PublishType;
import com.siemens.nextwork.tag.exception.ResourceNotFoundException;
import com.siemens.nextwork.tag.exception.RestForbiddenException;
import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Workstream;
import com.siemens.nextwork.tag.repo.NextWorkUserRepository;
import com.siemens.nextwork.tag.repo.RolesRepository;
import com.siemens.nextwork.tag.repo.WorkStreamRepository;
import com.siemens.nextwork.tag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_EXIST = "The user doesn't exist";
    @Autowired
    private NextWorkUserRepository nextWorkUserRepository;
    @Autowired
    private WorkStreamRepository workStreamRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public String findByEmail(String email) {
        Optional<NextWorkUser> nextWorkUser = nextWorkUserRepository.findByEmail(email);

        if (nextWorkUser.isPresent() && nextWorkUser.get().getStatus().equalsIgnoreCase(NextworkConstants.ACTIVE))
            return nextWorkUser.get().getId();
        else
            throw new ResourceNotFoundException(USER_NOT_EXIST);
    }

    @Override
    public String getUserRoleByEmail(String userEmail) {
        Optional<NextWorkUser> nextWorkUser = nextWorkUserRepository.findByEmail(userEmail);

        if (nextWorkUser.isPresent() && nextWorkUser.get().getStatus().equalsIgnoreCase(NextworkConstants.ACTIVE))
            return nextWorkUser.get().getRolesDetails().get(0).getRoleType();
        else
            throw new ResourceNotFoundException(USER_NOT_EXIST);

    }

    @Override
    public NextWorkUser getUserByEmail(String userEmail) {
        Optional<NextWorkUser> nextWorkUser = nextWorkUserRepository.findByEmail(userEmail);

        if (nextWorkUser.isPresent() && nextWorkUser.get().getStatus().equalsIgnoreCase(NextworkConstants.ACTIVE))
            return nextWorkUser.get();
        else
            throw new ResourceNotFoundException(USER_NOT_EXIST);

    }

    @Override
    public Workstream checkWorkStreamMemberOwnerUnPublishedWorkstream(String workStreamId, String email) {
        Workstream ws = null;
        Optional<NextWorkUser> user = nextWorkUserRepository.findByEmail(email);
        List<String> workStreamIds = new ArrayList<>();
        if (user.isPresent() && user.get().getStatus().equalsIgnoreCase("Active")
                && null != user.get().getRolesDetails() && !user.get().getRolesDetails().isEmpty()) {

            Optional<Workstream> workStreamOpt = workStreamRepository.findById(workStreamId);
            if (!workStreamOpt.isPresent() || workStreamOpt.get().getIsDeleted().equals(true)) {
                throw new ResourceNotFoundException(NextworkConstants.WORKSTREAM_NOT_FOUND);
            }
            ws = workStreamOpt.get();
            if (ws.getPublishedStatus().equals(PublishType.PUBLISH)) {
                throw new RestForbiddenException("This workstream is Publish.");
            }

            if (null != user.get().getWorkStreamList() && !user.get().getWorkStreamList().isEmpty()) {
                workStreamIds = user.get().getWorkStreamList().stream().map(WorkStreamDTO::getUid).toList();
            }
            if (!(workStreamIds.contains(workStreamId))) {
                throw new RestForbiddenException("User is not OWNER/MEMBER of this project.");

            }
        } else {
            throw new RestForbiddenException("Either User doesn't exists/ Inactive/ Doesn't have any role assigned.");
        }
        return ws;
    }
}
