package com.ccz.modules.controller.channel;

import com.ccz.modules.controller.common.CommonForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public class ChannelForm {

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelIdForm extends CommonForm {
        private String userName;
        private String channelId;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelCreateForm extends CommonForm {
        private String userName;
        private List<String> attendees;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelExitForm extends ChannelIdForm {
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelEnterForm extends ChannelIdForm {
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelInviteForm extends ChannelIdForm{
        private List<String> attendees;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class MyChannelForm extends CommonForm {
        private String userName;
        private int offset;
        private int count;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelLastMessageForm extends ChannelIdForm{
        private List<String> channelIds;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChannelInfoForm extends MyChannelForm {
    }
}
