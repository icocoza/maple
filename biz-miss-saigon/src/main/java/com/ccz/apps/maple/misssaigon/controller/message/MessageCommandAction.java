package com.ccz.apps.maple.misssaigon.controller.message;

import com.ccz.apps.maple.misssaigon.repository.channel.ChannelRepository;
import com.ccz.apps.maple.misssaigon.repository.message.MessageRepository;
import com.ccz.apps.maple.misssaigon.repository.user.UserRepository;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.controller.admin.AdminCommandCommonAction;
import com.ccz.modules.controller.message.MessageCommandCommonAction;
import com.ccz.modules.repository.db.channel.ChannelCommonRepository;
import com.ccz.modules.repository.db.message.MessageCommonRepository;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MessageCommandAction extends MessageCommandCommonAction {

    @Autowired
    MessageRepository messageCommonRepository;
    @Autowired
    UserRepository userCommonRepository;
    @Autowired
    ChannelRepository channelCommonRepository;

    @PostConstruct
    public void init() {
        super.initCommandFunctions(messageCommonRepository, userCommonRepository, channelCommonRepository);
    }

}
