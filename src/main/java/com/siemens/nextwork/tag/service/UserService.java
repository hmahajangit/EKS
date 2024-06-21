package com.siemens.nextwork.tag.service;

import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Workstream;


public interface UserService {

    String findByEmail(String email);

    String getUserRoleByEmail(String userEmail);

    NextWorkUser getUserByEmail(String userEmail);

    public Workstream checkWorkStreamMemberOwnerUnPublishedWorkstream(String workStreamId, String email);

}