package com.ccz.apps.maple.misssaigon.controller.friend;

import com.ccz.apps.maple.misssaigon.repository.friend.FriendRepository;
import com.ccz.apps.maple.misssaigon.repository.user.UserRepository;
import com.ccz.modules.controller.admin.AdminCommandCommonAction;
import com.ccz.modules.controller.friend.FriendCommandCommonAction;
import com.ccz.modules.repository.db.friend.FriendCommonRepository;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FriendCommandAction extends FriendCommandCommonAction {
    @Autowired
    FriendRepository friendCommonRepository;
    @Autowired
    UserRepository userCommonRepository;

    @PostConstruct
    public void init() {
        super.initCommandFunctions(friendCommonRepository, userCommonRepository);
    }

}
