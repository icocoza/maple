package com.ccz.apps.maple.misssaigon.controller.channel;

import com.ccz.apps.maple.misssaigon.repository.channel.ChannelRepository;
import com.ccz.apps.maple.misssaigon.repository.friend.FriendRepository;
import com.ccz.apps.maple.misssaigon.repository.user.UserRepository;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.controller.admin.AdminCommandCommonAction;
import com.ccz.modules.controller.channel.ChannelCommandCommonAction;
import com.ccz.modules.repository.db.channel.ChannelCommonRepository;
import com.ccz.modules.repository.db.friend.FriendCommonRepository;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ChannelCommandAction extends ChannelCommandCommonAction {
    @Autowired
    ChannelRepository channelCommonRepository;
    @Autowired
    FriendRepository friendCommonRepository;
    @Autowired
    UserRepository userCommonRepository;


    @PostConstruct
    public void init() {
        super.initCommandFunctions(channelCommonRepository, friendCommonRepository, userCommonRepository);
    }

}
