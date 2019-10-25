package com.ccz.modules.controller.channel;

import com.ccz.modules.controller.common.CommonForm;
import lombok.Data;

import java.util.List;

public class ChannelForm {

    @Data
    public class ChannelIdForm extends CommonForm {
        private String userName;
        private String channelId;
    }

    @Data
    public class ChannelCreateForm extends CommonForm {
        private String userName;
        private List<String> attendees;
    }

    @Data
    public class ChannelExitForm extends ChannelIdForm {
    }

    @Data
    public class ChannelEnterForm extends ChannelIdForm {
    }

    @Data
    public class ChannelInviteForm extends ChannelIdForm{
        private List<String> attendees;
    }

    @Data
    public class MyChannelForm extends CommonForm {
        private String userName;
        private int offset;
        private int count;
    }

    @Data
    public class ChannelLastMessageForm extends ChannelIdForm{
        private List<String> channelIds;
    }

    @Data
    public class ChannelInfoForm extends MyChannelForm {
    }
}
